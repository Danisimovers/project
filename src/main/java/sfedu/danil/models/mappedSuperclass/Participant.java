package sfedu.danil.models.mappedSuperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Participant extends User {

    private String fishingExperience;

    public Participant() {
        super();
    }

    public Participant(String name, String email, String phoneNumber, String rating, String competitionId, String fishingExperience) {
        super(name, email, phoneNumber, rating, competitionId);
        this.fishingExperience = fishingExperience;
    }
}
