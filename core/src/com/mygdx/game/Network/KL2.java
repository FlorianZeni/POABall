package com.mygdx.game.Network;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import java.util.ArrayList;


public class KL2 extends JFrame implements KeyListener{

    ArrayList<Integer> strL = new ArrayList<>();

    KL2(ArrayList<Integer> str){
        strL = str;
        addKeyListener (this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 300);
        setVisible(true);
    }

    public void keyPressed(KeyEvent evt){
            /*System.out.println(evt);
            System.out.println(evt.getKeyCode());
            System.out.println(evt.getKeyText(evt.getKeyCode()) + "\n");*/

            //strL.add(evt.getKeyCode() + " " + evt.getKeyText(evt.getKeyCode()));
            strL.add(evt.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e){
    }

    @Override
    public void keyTyped(KeyEvent e){
    }
}
