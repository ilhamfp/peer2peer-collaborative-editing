package org.sister.p2pcollaborative;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.java_websocket.server.WebSocketServer;

import javax.swing.event.DocumentEvent;
import javax.swing.text.Document;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Controller{

    private static Controller controller;
    private String host = "localhost";
    private int port = 8887;
    private WebSocketServer server;
    private CRDT crdt;
    private Editor editor;
    private WebSocketClient client;

    public void onMessage(String operation, Character character) {
        if (operation.equalsIgnoreCase("I")) {
            crdt.remoteInsert(character);
        } else {
            crdt.remoteDelete(character);
        }
    }

    private Controller() {

    }


    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public void run() {
        // run server


        server = new ServerWebSocket(new InetSocketAddress(host, port));
        server.start();
        // run client
        System.out.println("hsdjfklfdjsa");
        client = null;
        try {
            client = new ClientWebSocket(new URI("ws://localhost:8887"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        client.connect();

        // test
//        List<Integer> l1 = List.of(1,1,2,3);
//        List<Integer> l2 = List.of(1,1,3,1,1,1,1);
//        List<Integer> l3 = Character.generatePositionBetween(l1, l2);
//        List<Integer> l4 = Character.generatePositionBetween(l3, l2);
//        List<Integer> l5 = Character.generatePositionBetween(l2, new ArrayList<>());
//
//        Character character1 = new Character('a', l1, Generators.timeBasedGenerator().generate());
//        Character character2 = new Character('b', l2, Generators.timeBasedGenerator().generate());
//        Character character3 = new Character('c', l3, Generators.timeBasedGenerator().generate());
//        Character character4 = new Character('d', l4, Generators.timeBasedGenerator().generate());
//        Character character5 = new Character('e', l5, Generators.timeBasedGenerator().generate());
//        crdt = new CRDT();
//
////        System.out.println("AFTER 1");
//        crdt.remoteInsert(character1);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER 2");
//        crdt.remoteInsert(character2);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER 3");
//        crdt.remoteInsert(character3);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER 4");
//        crdt.remoteInsert(character4);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER 5");
//        crdt.remoteInsert(character5);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER DELETE 3");
//        crdt.remoteDelete(character3);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });
//
////        System.out.println("AFTER DELETE 1");
//        crdt.remoteDelete(character1);
//        crdt.getCharacters().forEach(c -> {
////            System.out.println(c.getValue());
////            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
//        });

        editor = new Editor();
        editor.setDocumentListener(new Editor.DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                int position = editor.getPosition();
                client.send("I" + new Gson().toJson(crdt.localInsert(editor.getText().charAt(position), position)));
            }
            public void removeUpdate(DocumentEvent e) {
                int position = editor.getPosition()-1;
                crdt.localDelete(position);
                client.send("R" + position);

            }
            public void changedUpdate(DocumentEvent e) {
                //Plain text components do not fire these events

            }
        });
    }

    public static void main(String[] args) {
        Controller.getInstance().run();
    }
}
