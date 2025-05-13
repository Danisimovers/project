package sfedu.danil.models.mappedTableperclass;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "DayCatch")
public class DayCatch extends Catch {

    private LocalDateTime dayTime;

    public DayCatch(String fishType, double weight, double points, String userId, String competitionId, LocalDateTime dayTime) {
        super(fishType, weight, points, userId, competitionId);
        this.dayTime = dayTime;
    }

    public DayCatch() {
        super();
    }
}
