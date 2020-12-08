package com.mygdx.game.Network2;

public class ClientMessage {
    public boolean[] inputs;

    public ClientMessage(boolean[] inputs){
        this.inputs = inputs;
    }

    public ClientMessage(){}

    public boolean[] getInputs() {
        return inputs;
    }
}
