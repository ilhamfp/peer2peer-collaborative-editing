package org.sister.p2pcollaborative;

import java.util.UUID;

public class VersionVector {
    private int counter;
    private UUID siteID;

    public VersionVector(int counter, UUID siteID) {
        this.counter = counter;
        this.siteID = siteID;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public UUID getSiteID() {
        return siteID;
    }

    public void setSiteID(UUID siteID) {
        this.siteID = siteID;
    }
}
