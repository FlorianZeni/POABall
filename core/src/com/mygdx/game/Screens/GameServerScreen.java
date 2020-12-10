package com.mygdx.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.Network2.ServerKryo;


public class GameServerScreen extends GameScreen {

    private float[] serverMessage = new float[2 + playersAmount * 2];
    private boolean[][] playerInputs = new boolean[playersAmount][5];
    ServerKryo server;

    public GameServerScreen(Game aGame) {
        super(aGame);
        server = new ServerKryo(playersAmount);
    }

    @Override
    protected void update(float deltaTime) {

        timeSinceLastUpdate += deltaTime;

        if(timeSinceLastUpdate > timeBetweenUpdates) {
            getActorsPositions();
            server.sendActorPositions(serverMessage);
            getHostInputs();
            getClientsInputs();
            applyPlayerInputs(deltaTime);
            if(checkForGoal()){
                resetActors();
            }
        }

        super.update(deltaTime);

    }

    private void getActorsPositions(){
        serverMessage[0] = ball.getPosition().x;
        serverMessage[1] = ball.getPosition().y;
        for(int i = 0; i < playersAmount; i++){
            serverMessage[(i * 2) + 2] = playerList[i].getPosition().x;
            serverMessage[(i * 2) + 3] = playerList[i].getPosition().y;
        }
    }

    private void applyPlayerInputs(float deltatime){
        Vector2 direction;
        //System.out.println(Arrays.deepToString(playerInputs));
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

    private void getClientsInputs(){
        for(int i = 1; i < playersAmount; i ++){
            this.playerInputs[i] = server.getPlayerInput()[i-1];
        }

        //System.out.println("Player Inputs : " + Arrays.deepToString(this.playerInputs));
    }

    private boolean checkForGoal(){
        if(ball.getPosition().x < 0){
            scoreRed += 1;
            return true;
        }
        else if(ball.getPosition().x > SCENE_WIDTH){
            scoreBlue += 1;
            return true;
        }
        return false;
    }

    private void resetActors(){
        ball.setClientPosition(SCENE_WIDTH / 2, SCENE_HEIGHT / 2);
        ball.resetVelocity();

    }

    @Override
    public void dispose(){
        server.terminate();
        super.dispose();
    }

}
