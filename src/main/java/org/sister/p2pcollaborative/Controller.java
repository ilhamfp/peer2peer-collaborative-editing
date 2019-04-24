package org.sister.p2pcollaborative;

import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;
import org.sister.p2pcollaborative.model.LocalCharacter;

import javax.swing.event.DocumentEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Controller{

    private static Controller controller;
    private int port = 8883;
    private Messenger messenger;
    private CRDT crdt;
    private Editor editor;
    private List<VersionVector> versionVectors = new ArrayList<>();
    private Integer counter = 0;

    public CRDT getCrdt() {
        return crdt;
    }

    public List<VersionVector> getVersionVectors() {
        return versionVectors;
    }

    public void onMessage(String operation, Character character) {
        if (operation.equalsIgnoreCase("I")) {
            increaseCounter(character.getSiteId());
            LocalCharacter localCharacter = crdt.remoteInsert(character);
            editor.insertChar(localCharacter.getValue(), localCharacter.getIndex());
        } else {
            editor.deleteChar(crdt.remoteDelete(character));
        }
    }

    public void increaseCounter(UUID uuid) {
        for (VersionVector vector : versionVectors) {
            if (vector.getSiteId().equals(uuid)) {
                vector.increaseCounter();
                break;
            }
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
//        messenger.start();

        editor = new Editor();
        editor.setKeyListener(new Editor.KeyListener() {

            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }
            @Override
            public void keyPressed(KeyEvent keyEvent) {
                int position = editor.getT().getCaretPosition();
                int code = 	keyEvent.getKeyCode();
                char c = keyEvent.getKeyChar();
                if (code == keyEvent.VK_BACK_SPACE){
                    if (position > 0) {
                        System.out.println("Remove at " + (position - 1));
                        messenger.sendToClient("R" + new Gson().toJson(crdt.localDelete(position-1)));
                    }
                } else if (code == keyEvent.VK_DELETE){
                    if (position < editor.getT().getText().length()){
                        System.out.println("Remove at " + (position));
                        messenger.sendToClient("R" + new Gson().toJson(crdt.localDelete(position)));
                    }
                }else if (c != keyEvent.CHAR_UNDEFINED){
                    System.out.println("Insert " + c + " at " + position);
                    Character ch = crdt.localInsert(c, position);
                    messenger.sendToClient("I" + new Gson().toJson(ch));
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
//                int position = editor.getT().getCaretPosition()-1;
//                Character c = crdt.localInsert(editor.getT().getText().charAt(position), position);
//                messenger.sendToClient("I" + new Gson().toJson(c));
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