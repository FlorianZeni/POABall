package com.mygdx.game.Tchat;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ServeurChat extends Listener {
    static Server serveur;
    static int udpPort = 54555, tcpPort = 54777;
    private  static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


    public static void start(){
        try {
            serveur = new Server();
            Registre.registre(serveur.getKryo());
            serveur.bind(tcpPort, udpPort);
            serveur.start();
            serveur.addListener(new ServeurChat());
            ChatBox.isConnected = true;
            ChatBox.tachat.append("["+ LocalTime.now().format(dtf) + "  Connecté comme serveur]\n");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void send (Object o){
        if(o instanceof Message){
            Message m = (Message) o;
            ChatBox.tachat.append(m.getMessage());
            serveur.sendToAllTCP(m);
        }

    }

    public void connected(Connection connection){
        ChatBox.tachat.append("["+ connection.getRemoteAddressTCP().getHostString() + " Connecté]\n");
    }

    public void received(Connection connection, Object o){
        if(o instanceof Message){
            Message m = (Message) o;
            ChatBox.tachat.append(m.getMessage());
            serveur.sendToAllExceptTCP(connection.getID(), m);
        }
    }

    public void disconnected(Connection connection){
        ChatBox.tachat.append("[" + connection.getID() +" " + "Déconnecté]\n");
    }

    public static void close(){
        ChatBox.tachat.append("[Déconnection]");
    }

}
