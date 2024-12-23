package sfedu.danil.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Competition {
    private String id;
    private String name;
    private LocalDateTime date;

    public Competition(String name, LocalDateTime date) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.date = date;

    }
    public Competition() {
    }
}
