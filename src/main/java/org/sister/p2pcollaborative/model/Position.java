package org.sister.p2pcollaborative.model;

import java.util.UUID;

public class Position {

    private Integer digit;
    private UUID siteId;

    public Position(Integer digit, UUID siteId) {
        this.digit = digit;
        this.siteId = siteId;
    }

    public Integer getDigit() {
        return digit;
    }

    public void setDigit(Integer digit) {
        this.digit = digit;
    }

    public UUID getSiteId() {
        return siteId;
    }

    public void setSiteId(UUID siteId) {
        this.siteId = siteId;
    }
}
