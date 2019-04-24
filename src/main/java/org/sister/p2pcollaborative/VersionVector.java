package org.sister.p2pcollaborative;

import java.util.UUID;

public class VersionVector {

    private UUID siteId;
    private int counter;

    public VersionVector(UUID siteId, int counter) {
        this.siteId = siteId;
        this.counter = counter;
    }

    public UUID getSiteId() {
        return siteId;
    }

    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public void increaseCounter() {
        this.counter++;
    }
}
