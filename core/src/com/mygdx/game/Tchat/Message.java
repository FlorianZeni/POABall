package com.mygdx.game.Tchat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Message {
    String absender = "", text="";
    LocalTime time;
    private  static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
    public Message(){};
    public Message(String absender, String text){
        this.absender = absender;
        this.text = text;
    };

    public String getMessage(){
        return "[" + time.format(dtf) + " " + absender + "] " + text + "\n";
        //return "[" + absender + "]" + text + "\n";
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getAbsender() {
        return absender;
    }

    public void setAbsender(String absender) {
        this.absender = absender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
