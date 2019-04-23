package org.sister.p2pcollaborative;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Character {
    private char value;
    private List<Integer> position;
    private UUID siteId;

    public Character(char value, List<Integer> position, UUID siteId) {
        this.value = value;
        this.position = position;
        this.siteId = siteId;
    }

    public char getValue() {
        return value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public UUID getSiteId() {
        return siteId;
    }

    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }

    public List<Integer> getPosition() {
        return position;
    }

    public void setPosition(List<Integer> position) {
        this.position = position;
    }

    public static List<Integer> longToArrayDigits(Long num) {
        List<Integer> result = new ArrayList<>();
        do {
            result.add(0, (int) (num%10));
            num = num/10;
        } while (num != 0);

        return result;
    }

    public static Long arrayDigitsToLong(List<Integer> nums, Integer totalDigit) {
        Long result = 0L;
        for (int i = 0; i < nums.size(); i++) {
            result = result*10 + nums.get(i);
        }
        for (int i = nums.size(); i < totalDigit; i++) {
            result *= 10;
        }
        return result;
    }

    public static List<Integer> generatePositionBetween(List<Integer> before, List<Integer> after) {
        Integer maxDigit = Math.max(before.size(), after.size());
        Long beforeLong = arrayDigitsToLong(before, maxDigit);
        Long afterLong = arrayDigitsToLong(after, maxDigit);
        if (Math.abs(afterLong - beforeLong) == 1) {
            afterLong *= 10;
            beforeLong *= 10;
        }
        if (before.size() == 0) {
            return longToArrayDigits(afterLong / 2);
        }
        else if (after.size() == 0) {
            return longToArrayDigits(beforeLong + beforeLong / 2);
        }
        else {
            return longToArrayDigits((beforeLong + afterLong) / 2);
        }
    }
}
