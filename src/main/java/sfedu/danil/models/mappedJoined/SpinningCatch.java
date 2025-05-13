package sfedu.danil.models.mappedJoined;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "SpinningCatch")
@PrimaryKeyJoinColumn(name = "catch_id") // внешний ключ на родительский класс
public class SpinningCatch extends Catch {

    private String lureType;
    private String rodType;

    public SpinningCatch(String fishType, double weight, double points, String userId, String competitionId, String lureType, String rodType) {
        super(fishType, weight, points, userId, competitionId);
        this.lureType = lureType;
        this.rodType = rodType;
    }

    public SpinningCatch() {
        super();
    }
}
