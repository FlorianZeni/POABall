package com.mygdx.game.Network2;

public class ServerMessage {
    public float[] actorsPositions;

    public ServerMessage(float[] actorsPositions){
        this.actorsPositions = actorsPositions;
    }

    public ServerMessage(){}

    public float[] getActorsPositions() {
        return actorsPositions;
    }
}
