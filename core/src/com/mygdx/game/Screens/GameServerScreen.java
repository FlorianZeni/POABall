package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Network.Serveur;

public class GameServerScreen extends GameScreen {

    private float[] serverMessage = new float[2 + playersAmount * 2];
    private boolean[][] playerInputs = new boolean[playersAmount][2 + playersAmount * 2];

    public GameServerScreen(Game aGame) {
        super(aGame);
        Serveur server = new Serveur();
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;

        if(timeSinceLastUpdate > timeBetweenUpdates) {
            getActorsPositions();
            //sendUDP(serverMessage);
            getHostInputs();
            applyPlayerInputs(deltaTime);
        }

        super.update(deltaTime);

    }

    private void getActorsPositions(){
        serverMessage[0] = ball.getPosition().x;
        serverMessage[1] = ball.getPosition().y;
        for(int i = 0; i < playersAmount; i+=2){
            serverMessage[i+2] = playerList[i].getPosition().x;
            serverMessage[i+3] = playerList[i].getPosition().y;
        }
    }

    private void onClientMessageReceived(boolean[] message, int client){
        playerInputs[client] = message;
    }

    private void applyPlayerInputs(float deltatime){
        Vector2 direction;

        for(int i = 0; i < playersAmount; i++){
            direction = new Vector2(0,0);
            if (playerInputs[i][0])
                direction.y += 1;
            if (playerInputs[i][1])
                direction.x += 1;
            if (playerInputs[i][2])
                direction.y -= 1;
            if (playerInputs[i][3])
                direction.x -= 1;
            if (playerInputs[i][4] & playerList[i].getBallDistance(ball) < 6)
                ball.shoot(playerList[i].body.getPosition());

            playerList[i].move(direction, deltatime);
        }
    }

    private void getHostInputs(){

        playerInputs[0][0] = Gdx.input.isKeyPressed(Input.Keys.UP);
        playerInputs[0][1] = Gdx.input.isKeyPressed(Input.Keys.RIGHT);
        playerInputs[0][2] = Gdx.input.isKeyPressed(Input.Keys.DOWN);
        playerInputs[0][3] = Gdx.input.isKeyPressed(Input.Keys.LEFT);
        playerInputs[0][4] = Gdx.input.isKeyPressed(Input.Keys.SPACE);

    }
}
