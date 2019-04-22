package org.sister.p2pcollaborative;

import com.fasterxml.uuid.Generators;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    public static void main(String[] args) {
        List<Integer> l1 = List.of(1,1,2,3);
        List<Integer> l2 = List.of(1,1,3,1,1,1,1);
        List<Integer> l3 = Character.generatePositionBetween(l1, l2);
        List<Integer> l4 = Character.generatePositionBetween(l3, l2);
        List<Integer> l5 = Character.generatePositionBetween(l2, new ArrayList<>());

        Character character1 = new Character('a', l1, Generators.timeBasedGenerator().generate());
        Character character2 = new Character('b', l2, Generators.timeBasedGenerator().generate());
        Character character3 = new Character('c', l3, Generators.timeBasedGenerator().generate());
        Character character4 = new Character('d', l4, Generators.timeBasedGenerator().generate());
        Character character5 = new Character('e', l5, Generators.timeBasedGenerator().generate());
        CRDT crdt = new CRDT();

        System.out.println("AFTER 1");
        crdt.remoteInsert(character1);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER 2");
        crdt.remoteInsert(character2);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER 3");
        crdt.remoteInsert(character3);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER 4");
        crdt.remoteInsert(character4);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER 5");
        crdt.remoteInsert(character5);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER DELETE 3");
        crdt.remoteDelete(character3);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });

        System.out.println("AFTER DELETE 1");
        crdt.remoteDelete(character1);
        crdt.getCharacters().forEach(c -> {
            System.out.println(c.getValue());
            System.out.println(Character.arrayDigitsToLong(c.getPosition(), c.getPosition().size()));
        });
    }
}
