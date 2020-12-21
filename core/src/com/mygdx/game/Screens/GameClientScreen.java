package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Network.ClientKryo;
import com.mygdx.game.Tchat.ChatBox;
import com.mygdx.game.Utils.FixSizedArrayList;
import com.mygdx.game.Utils.Functions;

public class GameClientScreen extends GameScreen {

    private boolean[] playerInputs = {false, false, false, false, false};
    private boolean inputChanged = true;

    private float[] serverAnswer = new float[2 + playersAmount * 2];

    private ClientKryo client;
    private ChatBox chatbox;

    private final int logsSize = 10;
    private FixSizedArrayList<Vector2> positionLogs = new FixSizedArrayList<>(logsSize);
    private int positionIndex;

    private Vector2 targetPos = new Vector2(0,0);
    private boolean clientInitialized = false;
    private int clientIndex = -1;
    private int serverResponseTime = 0;

    private boolean clientPredictionEnabled = true;

    public GameClientScreen(Game aGame) {
        super(aGame);
        client = new ClientKryo(playersAmount);
//        chatbox = new ChatBox("FlorianV2", false, "25.80.133.159");
//        chatbox.create();
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;

        if(clientInitialized) {

            getPlayerInputs();

            if(clientPredictionEnabled)
                applyPlayerInputs(deltaTime);

            if (timeSinceLastUpdate > timeBetweenUpdates || inputChanged) {
                if(inputChanged)
                    sendPlayerInputs();
                positionLogs.add(playerList[clientIndex + 1].getPosition());
                timeSinceLastUpdate = 0;
            }

            if (client.getServerUpdateAvailable()) {
                serverAnswer = client.getActorsPositions();
                //System.out.println("Server Answer ! " + Arrays.toString(serverAnswer));

                ball.setClientPosition(serverAnswer[0], serverAnswer[1]);

                for (int i = 0; i < playersAmount; i++) {

                    targetPos.x = serverAnswer[(i * 2) + 2];
                    targetPos.y = serverAnswer[(i * 2) + 3];
                    if (clientPredictionEnabled) {
                        // Server Reconciliation regarding the current player
                        if (i == clientIndex + 1) {
                            if (positionLogs.size() < logsSize) {
                                playerList[i].setClientPosition(targetPos);
                            } else {
                                if (Math.min(Functions.Distance(positionLogs.get(positionIndex), targetPos),
                                        Functions.Distance(playerList[i].getPosition(), targetPos)) > 0.5) {
                                    System.out.println("Pos Corrected");
                                    System.out.println("LOG : " + Functions.Distance(positionLogs.get(positionIndex), targetPos));
                                    System.out.println("CURRENT : " + Functions.Distance(playerList[i].getPosition(), targetPos));
                                    playerList[i].setClientPosition(targetPos);
                                }
                            }

                        } else {
                            playerList[i].setClientPosition(targetPos);
                        }
                    } else {
                        playerList[i].setClientPosition(targetPos);
                    }
                }
            }
        }
        else{
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
        //System.out.println("Sending player inputs to the server");
        //System.out.println(Arrays.toString(playerInputs));

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

//        Client shall not be abble to actually shoot the ball since its position is set by the server wherever it is.
//        if (playerInputs[4] & playerList[clientIndex + 1].getBallDistance(ball) < 6)
//            ball.shoot(playerList[clientIndex + 1].getPosition());

        playerList[clientIndex + 1].move(direction, deltatime);


    }

    @Override
    public void dispose(){
        client.terminate();
        super.dispose();
    }

}
