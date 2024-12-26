package sfedu.danil.models;


import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

@Getter
@Setter


public class User {

    private String id;

    private String name;

    private String email;

    private String phoneNumber;

    private Role role;

    private String rating;

    private String competitionId;

    public User(String name, String email, String phoneNumber, Role role, String rating, String competitionId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.rating = rating;
        this.competitionId = competitionId;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "', phoneNumber='" + phoneNumber +
                "', role='" + role + "', rating=" + rating + ", competitionId=" + competitionId + "}";
    }



}
