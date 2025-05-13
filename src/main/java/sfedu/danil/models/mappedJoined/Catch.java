package sfedu.danil.models.mappedJoined;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "CatchJoined")
@Inheritance(strategy = InheritanceType.JOINED)
public class Catch {

    @Id
    private String id;


    private String fishType;
    private double weight;
    private double points;
    private String userId;
    private String competitionId;

    @ElementCollection
    @CollectionTable(
            name = "catch_images",
            joinColumns = @JoinColumn(name = "catch_id")
    )
    @Column(name = "filename")
    private Set<String> imageFilenames = new HashSet<>();

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
