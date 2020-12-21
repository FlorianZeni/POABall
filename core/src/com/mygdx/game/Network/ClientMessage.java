package com.mygdx.game.Network;

public class ClientMessage {
    private boolean[] inputs;

    public ClientMessage(boolean[] inputs){
        this.inputs = inputs;
    }

    public ClientMessage(){}

    public boolean[] getInputs() {
        return inputs;
    }
}
