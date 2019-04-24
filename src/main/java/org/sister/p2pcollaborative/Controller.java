package org.sister.p2pcollaborative;

import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;
import org.sister.p2pcollaborative.model.LocalCharacter;

import javax.swing.event.DocumentEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.*;

public class Controller{

    private static Controller controller;
    private int port = 8883;
    private Messenger messenger;
    private CRDT crdt;
    private Editor editor;
    private Map<UUID, VersionVector> versionVectors = new HashMap<>();
    private Integer counter = 0;

    private List<Character> deleteBUffer = new ArrayList<>();

    public CRDT getCrdt() {
        return crdt;
    }

    public Integer getCounter(){
        return counter;
    }

    public Map getVersionVectors() {
        return versionVectors;
    }

    public void testDeleteBuffer(String operation, Character character) {
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            increaseCounter(character.getSiteId());
            LocalCharacter localCharacter = crdt.remoteInsert(character);
            editor.insertChar(localCharacter.getValue(), localCharacter.getIndex());

        }).start();
    }

    public void onMessage(String operation, Character character) {
        if (operation.equalsIgnoreCase("I")) {

            // test delete buffer by delaying insert
//            testDeleteBuffer(operation, character);

            // normal
            increaseCounter(character.getSiteId());
            LocalCharacter localCharacter = crdt.remoteInsert(character);
            editor.insertChar(localCharacter.getValue(), localCharacter.getIndex());
        } else {
            deleteBUffer.add(character);
        }
    }

    public void startDeleteBufferWorker() {
        new Thread(() -> {
            while (true) {
                for (Character character : new ArrayList<>(deleteBUffer)) {
                    if (versionVectors.get(character.getSiteId()).getCounter() >= character.getVersionVector().getCounter()) {
                        int index = crdt.remoteDelete(character);
                        if (index >= 0){
                            editor.deleteChar(index);
                        }
                        deleteBUffer.remove(character);
                    }
                }
            }

        }).start();
    }

    public void increaseCounter(UUID uuid) {
        versionVectors.get(uuid).increaseCounter();
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
        versionVectors.put(crdt.getSiteId(), new VersionVector(crdt.getSiteId(), 0));
        messenger = new Messenger(port);
        messenger.start();

        startDeleteBufferWorker();

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

                counter++;
                versionVectors.get(crdt.getSiteId()).increaseCounter();

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

            }

        });
    }

    public static void main(String[] args) {
        Controller.getInstance().run();

    }
}