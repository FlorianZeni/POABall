package com.mygdx.game.Utils;

import java.util.ArrayList;

public class FixSizedArrayList<T> extends ArrayList<T> {
    private int limit;

    public FixSizedArrayList(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(T item) {
        if (this.size() == limit){
            this.remove(0);
        }
        super.add(item);
        return false;
    }
}

