package sfedu.danil.models.mappedTableperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "NightCatch")
public class NightCatch extends Catch {

    private LocalDateTime nightTime;

    public NightCatch(String fishType, double weight, double points, String userId, String competitionId, LocalDateTime nightTime) {
        super(fishType, weight, points, userId, competitionId);
        this.nightTime = nightTime;
    }

    public NightCatch() {
        super();
    }
}
