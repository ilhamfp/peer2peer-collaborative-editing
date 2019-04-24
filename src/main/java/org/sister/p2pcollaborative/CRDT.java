package org.sister.p2pcollaborative;

import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import org.sister.p2pcollaborative.model.Character;
import org.sister.p2pcollaborative.model.LocalCharacter;
import org.sister.p2pcollaborative.model.Position;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

public class CRDT {
    private UUID siteId = Generators.timeBasedGenerator().generate();
    private List<Character> characters = new ArrayList<>();
    private Integer base = 32;
    private Integer boundary = 10;
    private List<java.lang.Character> strategyCache = new ArrayList<>();
    private String strategy = "random";

    public UUID getSiteId() {
        return siteId;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Character localInsert(char value, int index) {
        List<Position> position;
        if (index == 0) {
            if (characters.size() == 0) {
                position = generatePositionBetween(new ArrayList<>(), new ArrayList<>());
            }
            else {
                position = generatePositionBetween(new ArrayList<>(), characters.get(index).getPosition());
            }
        }
        else if (index == characters.size()) {
            position = generatePositionBetween(characters.get(index-1).getPosition(), new ArrayList<>());
        }
        else {
            position = generatePositionBetween(characters.get(index-1).getPosition(),
                    characters.get(index).getPosition());
        }

        Controller controller = Controller.getInstance();

        Character character = new Character(value, position, siteId, new VersionVector(siteId, controller.getCounter()));
        characters.add(index, character);
        return character;
    }

    public Character localDelete(int index) {
        return characters.remove(index);
    }
//
    public LocalCharacter remoteInsert(Character character) {
        int index = findInsertIndex(character);
        System.out.println("INDEX " + index);
        characters.add(index, character);
        return new LocalCharacter(character.getValue(), index);
    }

    public int remoteDelete(Character character) {
        int index = findIndexByAbsolutePosition(character);
        characters.remove(index);
        return index;
    }

    public List<Position> generatePositionBetween(List<Position> before, List<Position> after) {
        return findPositionBetween(before, after, new ArrayList<>(), 0);
    }

    private List<Position> findPositionBetween(List<Position> pos1, List<Position> pos2, List<Position> newPos, Integer level) {
        Integer basis = (int) Math.pow(2, level) * base;
        char boundaryStrategy = retrieveStrategy(level);

        Position id1 = pos1.size() == 0 ? new Position(0, siteId) : pos1.get(0);
        Position id2 = pos2.size() == 0 ? new Position(basis, siteId) : pos2.get(0);
        System.out.println("BASIS " + basis);
        System.out.println(id1.getDigit() + " " + id2.getDigit());

        if (id2.getDigit() - id1.getDigit() > 1) {
            Integer newDigit = generateIdBetween(id1.getDigit(), id2.getDigit(), boundaryStrategy);
            newPos.add(new Position(newDigit, siteId));
            return newPos;
        }
        else if (id2.getDigit() - id1.getDigit() == 1) {
            newPos.add(id1);
            return findPositionBetween(pos1.subList(1, pos1.size()), new ArrayList<>(), newPos, level+1);
        }
        else if (id1.getDigit().equals(id2.getDigit())) {
            if (id1.getSiteId().compareTo(id2.getSiteId()) < 0) {
                newPos.add(id1);
                return findPositionBetween(pos1.subList(1, pos1.size()), new ArrayList<>(), newPos, level+1);
            }
            else if (id1.getSiteId().compareTo(id2.getSiteId()) == 0) {
                newPos.add(id1);
                return findPositionBetween(pos1.subList(1, pos1.size()), pos2.subList(1, pos2.size()), newPos, level+1);
            }
        }
        return newPos;
    }

    private Integer generateIdBetween(Integer min, Integer max, char boundaryStrategy) {
        if ((max - min) < this.boundary) {
            min = min + 1;
        } else {
            if (boundaryStrategy == '-') {
                min = max - this.boundary;
            } else {
                min = min + 1;
                max = min + this.boundary;
            }
        }
        return (int) Math.floor(Math.random() * (max - min)) + min;
    }

    private char retrieveStrategy(Integer level) {
        if (strategyCache.size() > level) {
            return strategyCache.get(level);
        }
        char stg;
        switch (strategy) {
            case "plus":
                stg = '+';
            case "minus":
                stg = '-';
            case "random":
                stg = Math.round(Math.random()) == 0 ? '+' : '-';
            default:
                stg = (level % 2) == 0 ? '+' : '-';
        }
        strategyCache.add(stg);
        return stg;
    }


    private int findInsertIndex(Character character) {
        for (int i = 0; i < characters.size(); i++) {
            for (int j = 0; j < characters.get(i).getPosition().size(); j++) {
                Integer charDigit = character.getPosition().get(j).getDigit();
                Integer charExistingDigit = characters.get(i).getPosition().get(j).getDigit();
                if (charDigit < charExistingDigit) {
                    return i;
                }
                else if (charDigit > charExistingDigit) {
                    break;
                }
            }
        }
        return characters.size();
    }

    private int findIndexByAbsolutePosition(Character character) {
        for (int i = 0; i < characters.size(); i++) {
            boolean found = true;
            Integer sizeCurrent = characters.get(i).getPosition().size();
            Integer sizeUpcoming = character.getPosition().size();
            if (!sizeCurrent.equals(sizeUpcoming)) {
                continue;
            }
            for (int j = 0; j < characters.get(i).getPosition().size(); j++) {
                if (!character.getPosition().get(j).getDigit().equals(characters.get(i).getPosition().get(j).getDigit())) {
                    found = false;
                    break;
                }
            }
            if (found) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {


    }
}
