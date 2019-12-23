package nbgame.game;

import javafx.animation.*;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import nbgame.constant.Dimension;
import nbgame.engine.Logicable;
import nbgame.engine.ShootLogic;
import nbgame.ship.Hit;
import nbgame.ship.Point;
import nbgame.ship.Ship;
import nbgame.ship.Tile;

import java.util.List;

public class Round {
    private final Game game;

    private Logicable shootLogic;
    private boolean animationEnd = true;
    private int gamerShotsCount;
    private int gamerResultCount;
    private int compShotsCount;
    private int compResultCount;

    public Round(Game game) {
        this.game = game;
        this.shootLogic = new ShootLogic(game);
    }

    public void run(Tile tile, int x, int y) {

        if (game.getStatus() != Status.RUN) return;

        if (!animationEnd) return;

        boolean shootCompAgain = true;
        SequentialTransition animPlay = new SequentialTransition();

        if (!updateGamerMove(tile, x, y)) {
            animationEnd = false;
            while(shootCompAgain) {
                shootCompAgain = updateCompMove(animPlay);
            }
            animPlay.setOnFinished(e -> animationEnd = true);
            animPlay.play();
        }
    }

    private boolean updateGamerMove(Tile tile, int x, int y) {
        Ship ship = null;
        boolean shootAgain = false;

        if (tile.getHit() != Hit.NONE) return true;

        tile.setHit(Hit.HIT);
        if (tile.getSipsCount() == 0) {
            gamerShotsCount++;
            game.getGraphicDriver().drawExplosion(game.getComputerBattleField().getCanvas(), x, y, Hit.NONE);
            game.getGameForm().getLblGamerShots().setText(String.valueOf(gamerShotsCount));
        } else {
            shootAgain = true;
            gamerResultCount++;
            ship = tile.getShipCollection().get(0);
            ship.setHit(tile.getShipCollection().get(0).getHit() + 1);
            game.getGraphicDriver().drawExplosion(game.getComputerBattleField().getCanvas(), x, y, Hit.HIT);
            game.getGameForm().getLblGamerResult().setText(String.valueOf(gamerResultCount));
        }

        if (checkForGamerWin(game.getComputerShips())) {
            game.getGameForm().getTxtStatus().setText("----- Congratulation, You WON " + game.getSettings().getName() + " -----");
            game.setStatus(Status.STOP);
            game.endGame();
            createAnimFinish();
            shootAgain = true;
        } else {
            printGamerMessage(ship);
        }

        return shootAgain;
    }

    private boolean updateCompMove(SequentialTransition animPlay) {
        boolean shootAgain = false;
        int shotX, shotY;
        int row, column;
        Tile tile;

        Point shootPoint = shootLogic.getNextShoot();
        row = shootPoint.getRow();
        column = shootPoint.getColumn();

        tile = game.getGamerBattleField().getTiles()[row][column];
        tile.setHit(Hit.HIT);
        shotX = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
        shotY = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

        if (tile.getSipsCount() == 0) {
            compShotsCount++;
            animPlay.getChildren().add(createAnimFrame(shotX, shotY, Hit.NONE));
            game.getGameForm().getLblCompShots().setText(String.valueOf(compShotsCount));
        } else {
            shootAgain = true;
            compResultCount++;
            Ship ship = tile.getShipCollection().get(0);
            ship.setHit(ship.getHit() + 1);
            animPlay.getChildren().add(createAnimFrame(shotX, shotY, Hit.HIT));
            game.getGameForm().getLblCompResult().setText(String.valueOf(compResultCount));

            shootLogic.buildStrategy(ship, shootPoint);
        }

        if (checkForGamerWin(game.getGamerShip())) {
            game.getGameForm().getTxtStatus().setText("##### Computer WON, You lost !!! ##### ");
            game.setStatus(Status.STOP);
            game.endGame();
            createAnimFinish();
            shootAgain = false;
        }

        return shootAgain;
    }

    private void printGamerMessage(Ship ship) {
        if (ship == null) {
            game.getGameForm().getTxtStatus().setText(game.getSettings().getName() + ", You missed!");
        } else if (ship.getHitResult() == Hit.HIT) {
            game.getGameForm().getTxtStatus().setText(game.getSettings().getName() + ", You hit, but You didn't sink. Shoot again!");
        } else if (ship.getHitResult() == Hit.HIT_AND_SINK) {
                game.getGameForm().getTxtStatus().setText("Congratulation " + game.getSettings().getName() + ", You hit and sink. Shoot again!");
        }
    }

    private boolean checkForGamerWin(List<Ship> ships) {
        return ships.stream()
                .allMatch(s -> s.getHitResult() == Hit.HIT_AND_SINK);
    }

    int getGamerResultCount() {
        return gamerResultCount;
    }

    int getCompResultCount() {
        return compResultCount;
    }

    private Timeline createAnimFrame(int x, int y, Hit hitType) {
        KeyValue[] keyValues = new KeyValue[2];

        keyValues[0] = new KeyValue(game.getComputerBattleField().getViewFinder().layoutXProperty(), y);
        keyValues[1] = new KeyValue(game.getComputerBattleField().getViewFinder().layoutYProperty(), x);
        KeyFrame kf = new KeyFrame(Duration.millis(250), e -> onFrameFinishEvent(x, y, hitType), keyValues);
        Timeline timeLine = new Timeline();
        timeLine.getKeyFrames().add(kf);

        return timeLine;
    }

    private void onFrameFinishEvent(int x, int y, Hit hitType) {
        game.getGraphicDriver().drawExplosion(game.getGamerBattleField().getCanvas(), x, y, hitType);
    }

    void createAnimFinish() {
        RotateTransition rotate = new RotateTransition(Duration.millis(1000), game.getGameForm().getTxtStatus());
        rotate.setByAngle(180);
        rotate.setAxis(Rotate.Y_AXIS);
        rotate.setCycleCount(2);
        rotate.setAutoReverse(true);
        rotate.play();
    }
}
