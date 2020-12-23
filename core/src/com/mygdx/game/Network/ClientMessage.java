package com.mygdx.game.Network;

public class ClientMessage {
    private boolean[] inputs;
    private long id;

    public ClientMessage(boolean[] inputs, long id){
        this.inputs = inputs;
        this.id = id;
    }

    public ClientMessage(){}

    public boolean[] getInputs() {
        return inputs;
    }

    public long getId(){return id;}
}
