package com.mygdx.game.Network2;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerKryo extends Listener {
    private static Server server;
    private static int tcpPort = 27960,  udpPort= 27961;

    private List<Connection> connections = new ArrayList<Connection>();
    private int playerAmount;
    private boolean[][] playerInput =  {{false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, false, false}};

//    public static void main(String[] args){
//        ServerKryo server = new ServerKryo();
//        server.sendActorPositions(new float[]{0,0,0,0,5,5,0,0,0,0,5,5});
//    }

    public ServerKryo(int playerAmount){
        this.playerAmount = playerAmount;

        server = new Server();

        server.getKryo().register(ClientMessage.class);
        server.getKryo().register(ServerMessage.class);
        server.getKryo().register(boolean[].class);
        server.getKryo().register(float[].class);

        server.addListener(this);

        try {
            server.bind(tcpPort, udpPort);
        } catch (IOException e) {
            System.out.println("Error while Binding Server");
            e.printStackTrace();
        }

        server.start();

        System.out.println("Server has been Initialized");
    }

    public void connected(Connection c){
        System.out.println("Received a connection from "+c.getRemoteAddressUDP().getHostString());

        if(!connections.contains(c)){
            connections.add(c);
            System.out.println("Added a new Connection to the list");
        }
    }

    public void received(Connection c, Object p){
        if(p instanceof ClientMessage){

            ClientMessage msg = (ClientMessage) p;
            playerInput[connections.indexOf(c)] = msg.getInputs();
            //System.out.println("Received A message from Client : " + Arrays.toString(msg.getInputs()));

        }
    }

    public void disconnected(Connection c){
        System.out.println("Server Spotted a Disconnected Client");
    }

    public void sendActorPositions(float[] actorPositions){
        //System.out.println("Sending positions to clients");
        ServerMessage msg = new ServerMessage(actorPositions);
        server.sendToAllUDP(msg);
    }

    public boolean[][] getPlayerInput(){
        return playerInput;
    }

    public void terminate(){
        server.close();
        server.stop();
    }
}
