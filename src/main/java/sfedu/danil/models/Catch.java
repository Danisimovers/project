package sfedu.danil.models;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class Catch {
    private String id;
    private String fishType;
    private double weight;
    private double points;
    private String userId;
    private String competitionId;

    public Catch(String fishType, double weight, double points, String userId, String competitionId) {
        this.id = UUID.randomUUID().toString();
        this.fishType = fishType;
        this.weight = weight;
        this.points = points;
        this.userId = userId;
        this.competitionId = competitionId;
    }

    public Catch() {
    }
}
