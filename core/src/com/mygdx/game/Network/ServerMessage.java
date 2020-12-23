package com.mygdx.game.Network;

public class ServerMessage {
    private float[] actorsPositions;
    private long id;

    public ServerMessage(float[] actorsPositions, long id){
        this.actorsPositions = actorsPositions;
        this.id = id;
    }

    public ServerMessage(){}

    public float[] getActorsPositions() {
        return actorsPositions;
    }

    public long getId(){return id;}
}
