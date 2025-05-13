package sfedu.danil.models.mappedSuperclass;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String phoneNumber;
    private String rating;
    private String competitionId;

    public User() {
        this.id = UUID.randomUUID().toString();
    }

    public User(String name, String email, String phoneNumber, String rating, String competitionId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.competitionId = competitionId;
    }
}
