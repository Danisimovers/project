package sfedu.danil.models.mappedSuperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Organizer extends User {

    private String organizationName;

    public Organizer() {
        super();
    }

    public Organizer(String name, String email, String phoneNumber, String rating, String competitionId, String organizationName) {
        super(name, email, phoneNumber, rating, competitionId);
        this.organizationName = organizationName;
    }
}
