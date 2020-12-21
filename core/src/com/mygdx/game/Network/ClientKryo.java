package com.mygdx.game.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class ClientKryo extends Listener {
    static Client client;
    static String ip = "localhost";
    static int tcpPort = 27960, udpPort = 27961;

    private float[] actorsPositions;
    private boolean serverUpdateAvailable = false;
    private int clientIndex = -1;
    private int serverResponseTime = 0;
    private int playerAmount;

    private Connection serverConnection;

    public ClientKryo(int playerAmount){
        System.out.println("Initializing Client");

        client = new Client();

        this.playerAmount = playerAmount;

        client.getKryo().register(ClientMessage.class);
        client.getKryo().register(ServerMessage.class);
        client.getKryo().register(boolean[].class);
        client.getKryo().register(float[].class);
        client.getKryo().register(int.class);


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
            c.getReturnTripTime();
            //System.out.println("Client Received Updated Actors Positions" + Arrays.toString(actorsPositions));
        }

        if(p instanceof Integer){
            c.updateReturnTripTime();
            serverConnection = c;
            System.out.println("Packet is " + p);
            clientIndex = (int) p;
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

    public int getClientIndex(){
        return clientIndex;
    }

    public int getServerResponseTime(){
        System.out.println("Response time is " + serverConnection.getReturnTripTime());
        return serverConnection.getReturnTripTime();
    }

    public void terminate(){
        client.close();
        client.stop();
    }
}
