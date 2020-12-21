package com.mygdx.game.Network;

public class ServerMessage {
    private float[] actorsPositions;

    public ServerMessage(float[] actorsPositions){
        this.actorsPositions = actorsPositions;
    }

    public ServerMessage(){}

    public float[] getActorsPositions() {
        return actorsPositions;
    }
}
