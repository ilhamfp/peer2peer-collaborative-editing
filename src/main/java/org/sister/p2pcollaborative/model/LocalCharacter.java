package org.sister.p2pcollaborative.model;

import java.net.InetAddress;
import java.net.UnknownHostException;

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

    public static void main(String[] args) {
        InetAddress localhost = null;
        try {
            localhost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("System IP Address : " +
                (localhost.getHostAddress()).trim());
    }
}
