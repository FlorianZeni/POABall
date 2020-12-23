package com.mygdx.game.Utils;

import java.util.ArrayList;

/* This container contains a limited amount of items,
    when full, adding another item removes the oldest item */
public class FixSizedArrayList<T> extends ArrayList<T> {
    private int limit;

    public FixSizedArrayList(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean add(T item) {
        if (this.size() == limit) {
            this.remove(0);
        }
        super.add(item);
        return false;
    }

    @Override
    public void add(int index, T item) {
        if (this.size() == limit) {
            this.remove(0);
        }
        super.add(index, item);
    }
}

