package sfedu.danil.models;


import lombok.Getter;
import lombok.Setter;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.UUID;

@Getter
@Setter

@Root
public class User {
    @Element
    private String id;
    @Element
    private String name;
    @Element
    private String email;
    @Element
    private String phoneNumber;
    @Element
    private Role role;
    @Element
    private String rating;
    @Element(required = false)
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
}
