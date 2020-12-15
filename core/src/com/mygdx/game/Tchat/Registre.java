package com.mygdx.game.Tchat;

import com.esotericsoftware.kryo.Kryo;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.util.Locale;

public class Registre {
    public static void registre(Kryo k){
        k.register(Message.class);
        k.register(LocalTime.class, new LocalTimeSerializer());
        k.register(DateTimeFormatter.class);
        k.register(Locale.class);
        k.register(DecimalStyle.class);
    }
}
