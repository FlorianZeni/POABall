package com.mygdx.game.Network;

public class ServerMessage {
    private float[] actorsPositions;
    private int id;

    public ServerMessage(float[] actorsPositions, int id){
        this.actorsPositions = actorsPositions;
        this.id = id;
    }

    public ServerMessage(){}

    public float[] getActorsPositions() {
        return actorsPositions;
    }

    public int getId(){return id;}
}
