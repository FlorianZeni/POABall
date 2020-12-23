package com.mygdx.game.Network;

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
    private int[] clientMsgIds = {-1, -1, -1, -1};
    private int playerAmount;
    private int msgId = 0;
    private boolean[][] playerInput =  {{false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, false, false},
                                        {false, false, false, false, false}};


    public ServerKryo(int playerAmount){
        this.playerAmount = playerAmount;

        server = new Server();

        server.getKryo().register(ClientMessage.class);
        server.getKryo().register(ServerMessage.class);
        server.getKryo().register(boolean[].class);
        server.getKryo().register(float[].class);
        server.getKryo().register(int.class);

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

        c.sendTCP(connections.indexOf(c));
    }

    public void received(Connection c, Object p){
        if(p instanceof ClientMessage){

            ClientMessage msg = (ClientMessage) p;

            if(msg.getId() > clientMsgIds[connections.indexOf(c)]){
                System.out.println("Message has been accepted");
                playerInput[connections.indexOf(c)] = msg.getInputs();
                clientMsgIds[connections.indexOf(c)] = msg.getId();
            }else{
                System.out.println("An older message has been ignored");
            }
            //System.out.println("Received A message from Client : " + Arrays.toString(msg.getInputs()));
        }
    }

    public void disconnected(Connection c){
        System.out.println("Server Spotted a Disconnected Client");
    }

    public void sendActorPositions(float[] actorPositions){
        //System.out.println("Sending positions to clients");
        msgId ++;
        ServerMessage msg = new ServerMessage(actorPositions, msgId);
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
