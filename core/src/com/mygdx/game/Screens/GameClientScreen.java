package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Network.ClientKryo;
import com.mygdx.game.Tchat.ChatBox;
import com.mygdx.game.Utils.FixSizedArrayList;
import com.mygdx.game.Utils.Functions;
import org.graalvm.compiler.hotspot.stubs.VerifyOopStub;

public class GameClientScreen extends GameScreen {

    // Inputs Related Attributes
    private boolean[] playerInputs = {false, false, false, false, false};
    private boolean inputChanged = true;

    private float[] serverAnswer = new float[2 + playersAmount * 2];

    private ClientKryo client;
    private ChatBox chatbox;

    // Prediction Related Attributes
    private final int logsSize = 10;
    private final float positionTolerance = 0.25f;
    private FixSizedArrayList<Vector2> positionLogs = new FixSizedArrayList<>(logsSize);
    private int positionIndex;
    private Vector2 targetPos = new Vector2(0,0);

    // Interpolation Related Attributes
    private float timeSinceLastServerUpdate = 0;
    private float[] previousServerAnswer = new float[2 + playersAmount * 2];
    private float interpolationCoef;

    // Client Initialization Related Attributes
    private boolean clientInitialized = false;
    private int clientIndex = -1;
    private int serverResponseTime = 0;

    // Client Settings
    private final boolean clientPredictionEnabled = true;
    private final boolean interpolationEnabled = true;

    public GameClientScreen(Game aGame) {
        super(aGame);
        client = new ClientKryo(playersAmount);
//        chatbox = new ChatBox("FlorianV2", false, "25.80.133.159");
//        chatbox.create();
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;
        timeSinceLastServerUpdate += deltaTime;

        if(clientInitialized) {

            getPlayerInputs();

            if(clientPredictionEnabled)
                applyPlayerInputs(deltaTime);

            if(interpolationEnabled){
                // Actors Positions are updated at the update loop frequency
                interpolationCoef = Math.min(1, timeSinceLastServerUpdate / timeBetweenUpdates);

                ball.setClientPosition(interpolationCoef * serverAnswer[0] + (1 - interpolationCoef) * previousServerAnswer[0],
                                       interpolationCoef * serverAnswer[1] + (1 - interpolationCoef) * previousServerAnswer[1]);

                for (int i = 0; i < playersAmount; i++) {
                    targetPos.x = interpolationCoef * serverAnswer[(i * 2) + 2] + (1 - interpolationCoef) * previousServerAnswer[(i * 2) + 2];
                    targetPos.y = interpolationCoef * serverAnswer[(i * 2) + 3] + (1 - interpolationCoef) * previousServerAnswer[(i * 2) + 3];

                    if(i != clientIndex + 1)
                        playerList[i].setClientPosition(targetPos);
                }
            }

            if (timeSinceLastUpdate > timeBetweenUpdates || inputChanged) {
                // Sending new inputs to the server as soon as a change is detected
                if(inputChanged)
                    sendPlayerInputs();

                // Gathering previous positions for server reconciliation
                positionLogs.add(playerList[clientIndex + 1].getPosition());

                timeSinceLastUpdate = 0;
            }

            if (client.getServerUpdateAvailable()) {
                timeSinceLastServerUpdate = 0;

                // Keeping the previous position to allow interpolation
                previousServerAnswer = serverAnswer;

                // Fetch new Actors Positions from the kryonet Client
                serverAnswer = client.getActorsPositions();

                // Update actors positions as soon as the update is received
                if(!interpolationEnabled)
                    ball.setClientPosition(serverAnswer[0], serverAnswer[1]);

                for (int i = 0; i < playersAmount; i++) {

                    targetPos.x = serverAnswer[(i * 2) + 2];
                    targetPos.y = serverAnswer[(i * 2) + 3];

                    if (clientPredictionEnabled) {
                        // Server Reconciliation regarding the current player
                        if (i == clientIndex + 1) {
                            // Prediction is only used for the current player
                            if (positionLogs.size() < logsSize) {
                                // Not enabled until our positions log is built
                                playerList[i].setClientPosition(targetPos);
                            }
                            else {
                                if (Math.min(Functions.Distance(positionLogs.get(positionIndex), targetPos),
                                        Functions.Distance(playerList[i].getPosition(), targetPos)) > positionTolerance) {
                                    // If the distance is too great with the server's position, player is reset
                                    playerList[i].setClientPosition(targetPos);
                                }
                            }

                        } else {
                            // Updates other player's location
                            if(!interpolationEnabled)
                                playerList[i].setClientPosition(targetPos);
                        }
                    } else {
                        // Client prediction disabled
                        if(!interpolationEnabled)
                            playerList[i].setClientPosition(targetPos);
                    }
                }
            }
        }
        else{
            // Initialization
            if(timeSinceLastUpdate > timeBetweenUpdates){

                clientIndex = client.getClientIndex();
                serverResponseTime = client.getServerResponseTime();
                positionIndex = logsSize - 1 - Math.round(serverResponseTime / timeBetweenUpdates / 1000);

                if(clientIndex != -1){
                    clientInitialized = true;
                    System.out.println("Client has been initialized");
                }


                timeSinceLastUpdate = 0;

            }
        }

        super.update(deltaTime);

    }

    private void sendPlayerInputs(){
        client.sendClientInputs(playerInputs);
    }

    private void getPlayerInputs()
    {
        inputChanged = false;
        if(playerInputs[0] != Gdx.input.isKeyPressed(Input.Keys.UP)){
            playerInputs[0] = !playerInputs[0];
            inputChanged = true;
        }
        if(playerInputs[1] != Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            playerInputs[1] = !playerInputs[1];
            inputChanged = true;
        }
        if(playerInputs[2] != Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            playerInputs[2] = !playerInputs[2];
            inputChanged = true;
        }
        if(playerInputs[3] != Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            playerInputs[3] = !playerInputs[3];
            inputChanged = true;
        }
        if(playerInputs[4] != Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            playerInputs[4] = !playerInputs[4];
            inputChanged = true;
        }
    }

    private void applyPlayerInputs(float deltatime){

        Vector2 direction = new Vector2(0, 0);

        if (playerInputs[0])
            direction.y += 1;
        if (playerInputs[1])
            direction.x += 1;
        if (playerInputs[2])
            direction.y -= 1;
        if (playerInputs[3])
            direction.x -= 1;

        playerList[clientIndex + 1].move(direction, deltatime);
    }

    @Override
    public void dispose(){
        client.terminate();
        super.dispose();
    }
}
