package sfedu.danil.models.mappedSingletable;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity(name = "DayCompetition")
@DiscriminatorValue("Day")
public class DayCompetition extends Competition {

    public DayCompetition(String name, LocalDateTime date) {
        super(name, date);
    }

    public DayCompetition() {

    }
}