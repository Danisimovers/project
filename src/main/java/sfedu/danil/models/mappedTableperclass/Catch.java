package sfedu.danil.models.mappedTableperclass;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "CatchPerClass")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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
            name = "catch_image_mappings",
            joinColumns = @JoinColumn(name = "catch_id")
    )
    @MapKeyColumn(name = "filename")
    @Column(name = "image_description")
    private Map<String, String> imageDescriptions = new HashMap<>();

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
