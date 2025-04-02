package sfedu.danil.models;

import jakarta.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class Metadata {
    private String createdBy;
    private Date lastUpdated;

    public Metadata() {
    }

    public Metadata(String createdBy) {
        this.createdBy = createdBy;
        this.lastUpdated = new Date();
    }

    public void updateTimestamp() {
        this.lastUpdated = new Date();
    }
}
