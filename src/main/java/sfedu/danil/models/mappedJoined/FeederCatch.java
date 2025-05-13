package sfedu.danil.models.mappedJoined;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "FeederCatch")
@PrimaryKeyJoinColumn(name = "catch_id") // указываем внешний ключ, который ссылается на родительскую таблицу
public class FeederCatch extends Catch {

    private String baitType;
    private String rodType;

    public FeederCatch(String fishType, double weight, double points, String userId, String competitionId, String baitType, String rodType) {
        super(fishType, weight, points, userId, competitionId);
        this.baitType = baitType;
        this.rodType = rodType;
    }

    public FeederCatch() {
        super();
    }
}
