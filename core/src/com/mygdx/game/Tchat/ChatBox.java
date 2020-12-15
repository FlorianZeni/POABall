package com.mygdx.game.Tchat;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.Scanner;

public class ChatBox {
    int longueur = 500, largeur = 500;
    JFrame frame;
    JPanel panel;
    public Button bsend;
    public JTextField tfentree;
    public static JTextArea tachat;
    public JScrollPane spchat;
    public static boolean isHost, isConnected = false;
    public String pseudo;

    public ChatBox(String pseudo, boolean isHost){
        this.pseudo = pseudo;
        this.isHost = isHost;
    }

    public ChatBox(String pseudo, boolean isHost, String ip){
        this.pseudo = pseudo;
        this.isHost = isHost;
        if(!isHost){
            ClientChat.ip = ip;
        }

    }


    public void create(){
        /*
        System.out.println("entrer votre pseudo : ");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();*/

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        frame = new JFrame("ChatBox");
        frame.setSize(longueur,largeur);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);

        panel = new JPanel();
        panel.setBounds(0,0, longueur, largeur);
        panel.setLayout(null);

        bsend = new Button("Send");
        bsend.setBounds(375,420, 100, 25);
        bsend.setVisible(true);
        bsend.addActionListener(e->{
            if (isConnected && !tfentree.getText().isEmpty()){
                Message m =new Message(pseudo, tfentree.getText());
                m.setTime(LocalTime.now());
                if(isHost){
                    ServeurChat.send(m);
                }else{
                    ClientChat.send(m);
                }
                tfentree.setText("");
            }
        });
        panel.add(bsend);


        tfentree = new JTextField();
        tfentree.setBounds(10,420, 350, 25);
        tfentree.setVisible(true);
        panel.add(tfentree);

        tachat = new JTextArea();
        tachat.setVisible(true);

        spchat = new JScrollPane(tachat);
        spchat.setBounds(10,10, 350,400);
        spchat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(spchat);

        frame.setContentPane(panel);
        frame.setVisible(true);

/*
        System.out.println("entrer un nombre (0 si serveur)");
        Scanner entree = new Scanner(System.in);*/

        if (isHost){
            ServeurChat.start();
            isHost = true;
        }else{
            ClientChat.start();
            isHost = false;

        }

    }


}
