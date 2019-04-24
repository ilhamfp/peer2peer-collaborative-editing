package org.sister.p2pcollaborative.model;

import org.sister.p2pcollaborative.VersionVector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Character {
    private char value;
    private List<Position> position;
    private UUID siteId;
    private VersionVector versionVector;

    public Character(char value, List<Position> position, UUID siteId, VersionVector versionVector) {
        this.value = value;
        this.position = position;
        this.siteId = siteId;
        this.versionVector = versionVector;
    }

    public VersionVector getVersionVector() {
        return versionVector;
    }

    public void setVersionVector(VersionVector versionVector) {
        this.versionVector = versionVector;
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

    public List<Position> getPosition() {
        return position;
    }

    public void setPosition(List<Position> position) {
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



}
