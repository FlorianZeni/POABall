package com.mygdx.game.Network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;

public class Serveur{

    //public static void main(String[] args) throws IOException{
    public Serveur(){
        Server server = new Server();

        Kryo kryo = server.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        kryo.register(java.util.ArrayList.class);
        System.out.println("HELLLLLLLLLLLLLLLLO");
        server.start();
        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(new Listener(){
            public void received(Connection connection, Object object){
                if(object instanceof SomeRequest){
                    SomeRequest request = (SomeRequest)object;
                    System.out.println(request.list);

                    //System.out.println(object);

                    SomeResponse response = new SomeResponse();
                    //response.text = "Thanks!";
                    response.list.add(3);
                    response.list.add(4);
                    response.list.add(6);
                    response.list.add(9);
                    response.list.add(0);
                    response.list.add(3);
                    connection.sendTCP(response);
                }
            }
        });
    }

}
