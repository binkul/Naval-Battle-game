package nbgame.game;

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

    public int getFourMastCount() {
        return fourMastCount;
    }

    public int getThreeMastCount() {
        return threeMastCount;
    }

    public int getTwoMastCount() {
        return twoMastCount;
    }

    public String getName() {
        return name;
    }

    public int getOneMastCount() {
        return oneMastCount;
    }

    public Level getLevelOfDifficulty() {
        return levelOfDifficulty;
    }

    public void setFourMastCount(int fourMastCount) {
        this.fourMastCount = fourMastCount;
    }

    public void setThreeMastCount(int threeMastCount) {
        this.threeMastCount = threeMastCount;
    }

    public void setTwoMastCount(int twoMastCount) {
        this.twoMastCount = twoMastCount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevelOfDifficulty(Level levelOfDifficulty) {
        this.levelOfDifficulty = levelOfDifficulty;
    }

    public void setOneMastCount(int oneMastCount) {
        this.oneMastCount = oneMastCount;
    }
}
