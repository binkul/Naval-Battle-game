package nbgame.game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Settings {
    private int fourMastCount;
    private int threeMastCount;
    private int twoMastCount;
    private int oneMastCount;
    private String name;
    private Level levelOfDifficulty;

    public Settings(int fourMastCount, int threeMastCount, int twoMastCount, int oneMastCount, String name) {
        this.fourMastCount = fourMastCount;
        this.threeMastCount = threeMastCount;
        this.twoMastCount = twoMastCount;
        this.oneMastCount = oneMastCount;
        this.name = name;
        this.levelOfDifficulty = Level.LOW;
    }
}
