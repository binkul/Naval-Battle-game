package nbgame.game;

import nbgame.constant.FileAccess;
import nbgame.engine.*;
import nbgame.gui.GameForm;
import nbgame.gui.IntroForm;
import nbgame.ship.Ship;
import nbgame.user.BattleKeyboard;
import nbgame.user.BattleMouse;
import nbgame.user.Movable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Game {
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
        settings = new Settings(2, 3, 4, 4, "None");
        gameForm = new GameForm(this);
        shipArrangement = new ShipArrangement(this);
        graphicDriver = new GraphicDriver(this);
        fileDriver = new FileDriver();

        gamerShips = Init.createShips(settings);
        computerShips = RandomMixer.randomShip(settings);
        statistics = fileDriver.readAllResults();

        status = Status.STOP;
    }

    public void initGame() {
        PathDriver pathDriver = new PathDriver();
        IntroForm.open(pathDriver.getPicPath(FileAccess.SHIP_ICO_PATH));

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

        //graphicDriver.drawShipsPicOnField(computerShips, computerBattleField.getCanvas()); //tymczas odkryj statki

        Init.clearShipsContent(gamerShips);
        gamerBattleField.refreshFullBattleField(gamerShips);
        gamerBattleField.getViewFinder().setVisible(true);

        gameForm.resetResultsLabels();
        gameForm.getTxtStatus().setText("New game has started.");

        gameForm.getMenuArrange().setDisable(true);
        gameForm.getMenuSettings().setDisable(true);
        gameForm.getMenuStart().setDisable(true);
        gameForm.getMenuEnd().setDisable(false);

        round = null;
        round = new Round(this);

        status = Status.RUN;

        round.createAnimFinish();
    }

    public void endGame() {
        createStatistic();

        graphicDriver.drawAllAfterBattle(computerBattleField, computerShips);

        gameForm.getMenuArrange().setDisable(false);
        gameForm.getMenuSettings().setDisable(false);
        gameForm.getMenuStart().setDisable(false);
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

    public List<Ship> getGamerShip() {
        return gamerShips;
    }

    public List<Ship> getComputerShips() {
        return computerShips;
    }

    public void setGamerShips(List<Ship> gamerShips) {
        this.gamerShips = gamerShips;
    }

    public Status getStatus() {
        return status;
    }

    public Settings getSettings() {
        return settings;
    }

    public ShipArrangement getShipArrangement() {
        return shipArrangement;
    }

    public GraphicDriver getGraphicDriver() {
        return graphicDriver;
    }

    public BattleField getGamerBattleField() {
        return gamerBattleField;
    }

    public BattleField getComputerBattleField() {
        return computerBattleField;
    }

    public GameForm getGameForm() {
        return gameForm;
    }

    public Round getRound() {
        return round;
    }

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void updateSettings() {
            gamerShips = Init.createShips(settings);
            gamerBattleField.refreshFullBattleField(gamerShips);

            computerShips = RandomMixer.randomShip(settings);
            //computerBattleField.refreshBattleField(computerShips);
            computerBattleField.resetTiles();
            computerBattleField.putShipsOnTiles(computerShips);
    }

    public void gamerShipsRandom() {
        gamerShips = RandomMixer.randomShip(settings);
        gamerBattleField.refreshFullBattleField(gamerShips);
    }
}
