package org.sister.p2pcollaborative;

import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;
import org.sister.p2pcollaborative.model.LocalCharacter;

import javax.swing.event.DocumentEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller{

    private static Controller controller;
    private String host = "192.168.43.49";
    private int port = 8884;
    private Messenger messenger;
    private CRDT crdt;
    private Editor editor;

    private List<String> hosts = new ArrayList<>();
    private List<Integer> ports = new ArrayList<>();

    public void onMessage(String operation, Character character) {
        if (operation.equalsIgnoreCase("I")) {
            LocalCharacter localCharacter = crdt.remoteInsert(character);
            editor.insertChar(localCharacter.getValue(), localCharacter.getIndex());
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
        crdt = new CRDT();
        messenger = new Messenger(port);
        messenger.start();

        editor = new Editor();
        editor.setKeyListener(new Editor.KeyListener() {

            @Override
            public void keyTyped(KeyEvent keyEvent) {
                System.out.printf("civa");
//                int position = editor.getT().getCaretPosition();
//                Character c = crdt.localInsert(editor.getT().getText().charAt(position), position);
//                messenger.sendToClient("I" + new Gson().toJson(c));
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }

//            public void insertUpdate(DocumentEvent e) {
//                System.out.println("Woyowuo");
//                int position = editor.getT().getCaretPosition();
//                Character c = crdt.localInsert(editor.getT().getText().charAt(position), position);
//                sendToClient("I" + new Gson().toJson(c));
//            }
//            public void removeUpdate(DocumentEvent e) {
////                int position = editor.getPosition()-1;
////                sendToClient("R" + new Gson().toJson(crdt.localDelete(position)));
//            }
//            public void changedUpdate(DocumentEvent e) {
//
//            }
        });
    }

    public static void main(String[] args) {
        Controller.getInstance().run();

    }
}
