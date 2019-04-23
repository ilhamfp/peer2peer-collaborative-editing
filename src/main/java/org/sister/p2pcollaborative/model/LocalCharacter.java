package org.sister.p2pcollaborative.model;

public class LocalCharacter {
    private char value;
    private int index;

    public LocalCharacter(char value, int index) {
        this.value = value;
        this.index = index;
    }

    public char getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }
}
