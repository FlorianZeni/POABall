package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.mygdx.game.Bodies.Player;

import java.util.Arrays;

public class GameClientScreen extends GameScreen {

    private boolean[] playerInputs = {false, false, false, false, false};

    private boolean serverUpdateAvailable = false;
    private float[] serverAnswer = new float[2 + playersAmount * 2];

    public GameClientScreen(Game aGame) {
        super(aGame);
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;

        if(timeSinceLastUpdate > timeBetweenUpdates) {
            getPlayerInputs();
            sendPlayerInputs();
            timeSinceLastUpdate = 0;
        }

        if(serverUpdateAvailable){
            ball.setPosition(serverAnswer[0], serverAnswer[1]);
            for(int i = 0; i < playersAmount; i++){
                playerList[i].setPosition(serverAnswer[i + 2], serverAnswer[i + 2]);
            }
        }

        super.update(deltaTime);

    }

    private void sendPlayerInputs(){

        //sendUDP(playerInputs);
        System.out.println("Sending player inputs to the server");
        System.out.println(Arrays.toString(playerInputs));

    }

    private void getPlayerInputs(){

        playerInputs[0] = Gdx.input.isKeyPressed(Input.Keys.UP);
        playerInputs[1] = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        playerInputs[2] = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        playerInputs[3] = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        playerInputs[4] = Gdx.input.isKeyPressed(Input.Keys.SPACE);

    }

    private void onServerAnswer(float[] answer){

        serverAnswer = answer;
        serverUpdateAvailable = true;

    }
}
