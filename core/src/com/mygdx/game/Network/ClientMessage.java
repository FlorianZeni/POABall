package com.mygdx.game.Network;

public class ClientMessage {
    private boolean[] inputs;
    private int id;

    public ClientMessage(boolean[] inputs, int id){
        this.inputs = inputs;
        this.id = id;
    }

    public ClientMessage(){}

    public boolean[] getInputs() {
        return inputs;
    }

    public int getId(){return id;}
}
