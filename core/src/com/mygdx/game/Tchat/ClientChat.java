package com.mygdx.game.Tchat;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientChat extends Listener {
    static Client client;
    static int udpPort = 54555, tcpPort = 54777;
    public static String ip;
    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");

    public static void start(){
        try {
            client = new Client();
            Registre.registre(client.getKryo());
            client.start();
            client.connect(5000, ip, tcpPort, udpPort);
            client.addListener(new ClientChat());

            ChatBox.isConnected = true;
            ChatBox.tachat.append("["+ LocalTime.now().format(dtf) + "  Connecté comme Client]\n");

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void send(Object o){
        if(o instanceof Message){
            Message m = (Message)o;
            ChatBox.tachat.append(m.getMessage());
            client.sendTCP(m);
        }
    }

    public void received(Connection connection, Object o ){
        if(o instanceof Message) {
            Message m = (Message) o;
            ChatBox.tachat.append(m.getMessage());
        }
    }

    public void close(){
        ChatBox.tachat.append("[Deconnection]");
        client.close();
    }

}
