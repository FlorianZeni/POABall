package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Bodies.Player;
import com.mygdx.game.Network2.ClientKryo;

import java.util.Arrays;

public class GameClientScreen extends GameScreen {

    private boolean[] playerInputs = {false, false, false, false, false};

    private float[] serverAnswer = new float[2 + playersAmount * 2];

    private ClientKryo client;

    public GameClientScreen(Game aGame) {
        super(aGame);
        client = new ClientKryo(playersAmount);
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;

        if(timeSinceLastUpdate > timeBetweenUpdates) {
            getPlayerInputs();
            sendPlayerInputs();
            timeSinceLastUpdate = 0;
        }

        if(client.getServerUpdateAvailable()){
            serverAnswer = client.getActorsPositions();
            //System.out.println("Server Answer ! " + Arrays.toString(serverAnswer));
            ball.setClientPosition(serverAnswer[0], serverAnswer[1]);
            for(int i = 0; i < playersAmount; i++){
                playerList[i].setClientPosition(serverAnswer[(i * 2) + 2], serverAnswer[(i * 2) + 3]);
            }
        }

        super.update(deltaTime);

    }

    private void sendPlayerInputs(){
        client.sendClientInputs(playerInputs);
        System.out.println("Sending player inputs to the server");
        //System.out.println(Arrays.toString(playerInputs));

    }

    private void getPlayerInputs(){

        playerInputs[0] = Gdx.input.isKeyPressed(Input.Keys.UP);
        playerInputs[1] = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        playerInputs[2] = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        playerInputs[3] = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        playerInputs[4] = Gdx.input.isKeyPressed(Input.Keys.SPACE);

    }

}
