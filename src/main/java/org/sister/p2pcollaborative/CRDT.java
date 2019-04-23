package org.sister.p2pcollaborative;

import com.fasterxml.uuid.Generators;
import org.sister.p2pcollaborative.model.Character;
import org.sister.p2pcollaborative.model.LocalCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CRDT {
    private UUID siteId = Generators.timeBasedGenerator().generate();
    private List<Character> characters = new ArrayList<>();

    public UUID getSiteId() {
        return siteId;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public Character localInsert(char value, int index) {
        List<Integer> position;
        if (index == 0) {
            if (characters.size() == 0) {
                position = List.of(1);
            }
            else {
                position = Character.generatePositionBetween(new ArrayList<Integer>(), characters.get(index).getPosition());
            }
        }
        else if (index == characters.size()) {
            position = Character.generatePositionBetween(characters.get(index-1).getPosition(), new ArrayList<Integer>());
        }
        else {
            position = Character.generatePositionBetween(characters.get(index-1).getPosition(),
                    characters.get(index).getPosition());
        }
        Character character = new Character(value, position, siteId);
        characters.add(index, character);
        return character;
    }

    public Character localDelete(int index) {
        return characters.remove(index);
    }

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

    private int findInsertIndex(Character character) {
        if (characters.size() == 0) {
            return 0;
        }
        else {
            for (int i = 0; i < characters.size(); i++) {
                Character current = characters.get(i);
                Integer maxDigits = Math.max(current.getPosition().size(), character.getPosition().size());
                Long absPosition = Character.arrayDigitsToLong(character.getPosition(), maxDigits);
                if (Character.arrayDigitsToLong(current.getPosition(), maxDigits) >= absPosition) {
                    System.out.println("i " + i);
                    return i;
                }
            }
            return characters.size();
        }
    }

    private int findIndexByAbsolutePosition(Character character) {
        for (int i = 0; i < characters.size(); i++) {
            Integer maxDigits = Math.max(character.getPosition().size(), characters.get(i).getPosition().size());
            Long currentLong = Character.arrayDigitsToLong(characters.get(i).getPosition(), maxDigits);
            Long charLong = Character.arrayDigitsToLong(character.getPosition(), maxDigits);
            if (currentLong.equals(charLong)) {
                return i;
            }
        }
        return -1;
    }
}
