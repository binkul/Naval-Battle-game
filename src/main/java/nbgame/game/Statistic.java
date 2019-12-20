package nbgame.game;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Statistic implements Serializable {
    private String winner;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private int winnerResult;
    private int loserResult;

    public Statistic(String winner, String status, LocalDate date, LocalTime time, int winnerResult, int loserResult) {
        this.winner = winner;
        this.status = status;
        this.date = date;
        this.time = time;
        this.winnerResult = winnerResult;
        this.loserResult = loserResult;
    }

    public String getWinner() {
        return winner;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getWinnerResult() {
        return winnerResult;
    }

    public int getLoserResult() {
        return loserResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Statistic)) return false;
        Statistic statistic = (Statistic) o;
        return winnerResult == statistic.winnerResult &&
                loserResult == statistic.loserResult &&
                Objects.equals(winner, statistic.winner) &&
                Objects.equals(status, statistic.status) &&
                Objects.equals(date, statistic.date) &&
                Objects.equals(time, statistic.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(winner, status, date, time, winnerResult, loserResult);
    }

    @Override
    public String toString() {
        LocalTime timeNoMs = time.truncatedTo(ChronoUnit.MINUTES);
        return "Game result of " + date + " at " + timeNoMs + ", '" + winner + "' won by " + winnerResult + ":" + loserResult + ", game status: " + status;
    }
}
