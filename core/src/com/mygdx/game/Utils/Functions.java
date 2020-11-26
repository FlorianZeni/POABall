package com.mygdx.game.Utils;

import com.badlogic.gdx.math.Vector2;

public final class Functions {
    public static double Distance(Vector2 pos1, Vector2 pos2){
        return Math.sqrt(((pos1.x - pos2.x) * (pos1.x - pos2.x) + (pos1.y - pos2.y) * (pos1.y - pos2.y)));
    }
}
