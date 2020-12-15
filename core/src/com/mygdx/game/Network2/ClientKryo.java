package com.mygdx.game.Network2;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ClientKryo extends Listener {
    static Client client;
    static String ip = "25.80.133.159";
    static int tcpPort = 27960, udpPort = 27961;

    private float[] actorsPositions;
    private boolean serverUpdateAvailable = false;
    private int playerAmount;

//    public static vo25.103id main(String[] args){
//        ClientKryo client = new ClientKryo();
//        int help = 0;
//        while(help < 100){
//            help++;
//        }
//
//        client.sendClientInputs(new boolean[]{true, false, true, false, false});
//
//    }

    public ClientKryo(int playerAmount){
        System.out.println("Initializing CLient");

        client = new Client();

        this.playerAmount = playerAmount;

        client.getKryo().register(ClientMessage.class);
        client.getKryo().register(ServerMessage.class);
        client.getKryo().register(boolean[].class);
        client.getKryo().register(float[].class);

        client.start();

        try {
            client.connect(5000, ip, tcpPort, udpPort);
        } catch (IOException e) {
            System.out.println("Error while connecting the client");
            e.printStackTrace();
        }

        client.addListener(this);

        System.out.println("Client is now successfully connected");
    }

    public void connected(Connection c){
        /* This Method is, for some reasons, never called */
        System.out.println("Client connected");
    }

    public void received(Connection c, Object p){
        //System.out.println("Client received a packet");
        if(p instanceof ServerMessage){
            ServerMessage msg = (ServerMessage) p;
            actorsPositions = msg.getActorsPositions();
            serverUpdateAvailable = true;
            //System.out.println("Client Received Updated Actors Positions" + Arrays.toString(actorsPositions));
        }
    }

    public void sendClientInputs(boolean[] inputs){
        ClientMessage msg = new ClientMessage(inputs);
        client.sendUDP(msg);
        //System.out.println("Message sent " + Arrays.toString(msg.getInputs()));
    }

    public void disconnected(Connection c){
        System.out.println("Client Got Disconnected");
    }

    public boolean getServerUpdateAvailable(){
        return serverUpdateAvailable;
    }

    public float[] getActorsPositions(){
        serverUpdateAvailable = false;
        return actorsPositions;
    }

    public void terminate(){
        client.close();
        client.stop();
    }
}
