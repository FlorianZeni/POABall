package com.mygdx.game.Network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;
import java.util.ArrayList;

public class ClientKL {

    //public static void main(String[] args) throws IOException{
    public ClientKL(){
        ArrayList<Integer> strlist = new ArrayList<>();

        Client client = new Client();
        new KL2(strlist);

        Kryo kryo = client.getKryo();
        kryo.register(SomeRequest.class);
        kryo.register(SomeResponse.class);
        kryo.register(java.util.ArrayList.class);//

        client.start();
        try{
            client.connect(50000, "127.0.0.1", 54555, 54777);
        }catch(IOException e){
            e.printStackTrace();
        }

        client.addListener(new Listener(){
            public void received (Connection connection, Object object){
                if(object instanceof SomeResponse){
                    SomeResponse response = (SomeResponse)object;
                    System.out.println(response.list);
                }
            }
        });

        while(true){

            SomeRequest request = new SomeRequest();
            if(strlist.contains(39) == true){       // ->
                request.list.add(1);
            }
            else{
                request.list.add(0);
            }
            if(strlist.contains(37) == true){       // <-
                request.list.add(1);
            }
            else{
                request.list.add(0);
            }
            if(strlist.contains(38) == true){       // haut
                request.list.add(1);
            }
            else{
                request.list.add(0);
            }
            if(strlist.contains(40) == true){       // bas
                request.list.add(1);
            }
            else{
                request.list.add(0);
            }
            if(strlist.contains(32) == true){       // espace
                request.list.add(1);
            }
            else{
                request.list.add(0);
            }

            client.sendTCP(request);
            strlist.clear();

            /*if(strlist.size() != oldSize){
                String toSend = strlist.get(strlist.size()-1);
                client.sendTCP(toSend);
                //client.sendTCP(strlist);
                oldSize = strlist.size();
                //System.out.println(strlist);
            }*/

            //request.text = "Here is the request!";
            //client.sendTCP(request);

            try{
                Thread.sleep(500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}

//  envoyer [z, q, s, d, esp] sous forme de booléens, ex : [1, 0, 0, 1, 0]
// reçoit [x1, y1, x2, y2, ..., xb, yb]
