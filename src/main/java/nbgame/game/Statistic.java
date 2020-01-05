package nbgame.game;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Statistic implements Serializable {
    private String winner;
    private String status;
    private LocalDate date;
    private LocalTime time;
    private int winnerResult;
    private int loserResult;

    @Override
    public String toString() {
        LocalTime timeNoMs = time.truncatedTo(ChronoUnit.MINUTES);
        return "Game result of " + date + " at " + timeNoMs + ", '" + winner + "' won by " + winnerResult + ":" + loserResult + ", game status: " + status;
    }
}
