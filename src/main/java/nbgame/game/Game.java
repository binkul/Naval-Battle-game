package nbgame.game;

import lombok.Getter;
import lombok.Setter;
import nbgame.constant.FileAccess;
import nbgame.engine.*;
import nbgame.engine.graphic.GraphicDriver;
import nbgame.gui.GameForm;
import nbgame.gui.IntroForm;
import nbgame.ship.Ship;
import nbgame.user.BattleKeyboard;
import nbgame.user.BattleMouse;
import nbgame.user.Movable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class Game {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static int INITIAL_FOUR_MAST_SHIPS = 2;
    private final static int INITIAL_THREE_MAST_SHIPS = 3;
    private final static int INITIAL_TWO_MAST_SHIPS = 4;
    private final static int INITIAL_ONE_MAST_SHIPS = 4;
    private final static String INITIAL_NAME = "None";

    private final GameForm gameForm;
    private final ShipArrangement shipArrangement;
    private final GraphicDriver graphicDriver;
    private final Settings settings;
    private final FileDriver fileDriver;

    private final Movable userGameInterface = new BattleMouse(this);

    private final BattleField gamerBattleField = new BattleField(this, FileAccess.FIELD_WAVE_PIC_PATH);
    private final BattleField computerBattleField = new BattleField(this, FileAccess.FIELD_WAVE_PIC_PATH, userGameInterface);

    private Round round;
    private Status status;
    private List<Ship> gamerShips;
    private List<Ship> computerShips;
    private List<Statistic> statistics;

    public Game() {
        settings = new Settings(INITIAL_FOUR_MAST_SHIPS, INITIAL_THREE_MAST_SHIPS, INITIAL_TWO_MAST_SHIPS, INITIAL_ONE_MAST_SHIPS, INITIAL_NAME);
        gameForm = new GameForm(this);
        shipArrangement = new ShipArrangement(this);
        graphicDriver = new GraphicDriver();
        fileDriver = new FileDriver();

        gamerShips = RandomMixer.randomShip(settings);
        computerShips = RandomMixer.randomShip(settings);
        statistics = fileDriver.readAllResults();

        status = Status.STOP;
    }

    public void initGame() {
        PathDriver pathDriver = new PathDriver();
        IntroForm.open(pathDriver.getPath(FileAccess.SHIP_ICO_PATH));

        gamerBattleField.refreshFullBattleField(gamerShips);
        gamerBattleField.getViewFinder().setVisible(false);

        computerBattleField.putShipsOnTiles(computerShips);
        computerBattleField.getViewFinder().setVisible(false);

        gameForm.open();
    }

    public void startNewGame() {
        computerShips = RandomMixer.randomShip(settings);
        computerBattleField.refreshBattleField(computerShips);
        computerBattleField.getViewFinder().setVisible(true);

        Basic.clearShipsContent(gamerShips);
        gamerBattleField.refreshFullBattleField(gamerShips);
        gamerBattleField.getViewFinder().setVisible(true);

        gameForm.resetResultsLabels();
        gameForm.getTxtStatus().setText("New game has started.");

        gameForm.getMenuArrange().setDisable(true);
        gameForm.getMenuSettings().setDisable(true);
        gameForm.getMenuStart().setDisable(true);
        gameForm.getMenuLevel().setDisable(true);
        gameForm.getMenuEnd().setDisable(false);

        round = null;
        round = new Round(this);

        status = Status.RUN;
        LOG.info("New game started between {} and computer", settings.getName());

        round.createAnimFinish();
    }

    void endGame() {
        createStatistic();

        graphicDriver.drawAllAfterBattle(computerBattleField, computerShips);

        gameForm.getMenuArrange().setDisable(false);
        gameForm.getMenuSettings().setDisable(false);
        gameForm.getMenuStart().setDisable(false);
        gameForm.getMenuLevel().setDisable(false);
        gameForm.getMenuEnd().setDisable(true);
    }

    public void breakGame() {
        status = Status.BREAK;
        endGame();
    }

    private void createStatistic() {
        String winner = round.getGamerResultCount() >= round.getCompResultCount() ? settings.getName() : "Computer";
        String gameStatus = (status == Status.STOP) ? "Finished" : "Break";
        int winnerResult = Math.max(round.getGamerResultCount(), round.getCompResultCount());
        int loserResult = Math.min(round.getGamerResultCount(), round.getCompResultCount());

        Statistic statistic = new Statistic(winner, gameStatus, LocalDate.now(), LocalTime.now(), winnerResult, loserResult);
        fileDriver.writeResult(statistic);
        statistics = fileDriver.readAllResults();
    }

    public void updateSettings() {
            gamerShips = RandomMixer.randomShip(settings);
            gamerBattleField.refreshFullBattleField(gamerShips);

            computerShips = RandomMixer.randomShip(settings);
            computerBattleField.resetTiles();
            computerBattleField.putShipsOnTiles(computerShips);
    }

    public void gamerShipsRandom() {
        gamerShips = RandomMixer.randomShip(settings);
        gamerBattleField.refreshFullBattleField(gamerShips);
    }
}
