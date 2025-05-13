package sfedu.danil.models.mappedSingletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity(name = "NightCompetition")
@DiscriminatorValue("Night")
public class NightCompetition extends Competition {


    public NightCompetition(String name, LocalDateTime date) {
        super(name, date);
    }

    public NightCompetition() {

    }
}