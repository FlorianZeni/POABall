package com.mygdx.game.Tchat;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.time.LocalTime;

public class LocalTimeSerializer extends Serializer<LocalTime> {

    @Override
    public void write(Kryo kryo, Output output, LocalTime localTime) {
        output.writeString(localTime.toString());
    }

    @Override
    public LocalTime read(Kryo kryo, Input input, Class<LocalTime> aClass) {
        return LocalTime.parse(input.readString());
    }
}
