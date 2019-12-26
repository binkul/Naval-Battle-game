package nbgame.user;

import javafx.scene.paint.Color;
import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.ship.ShipStatus;
import nbgame.ship.Position;
import nbgame.ship.Tile;

import java.util.List;
import java.util.stream.Stream;

public class UserCommon {
    private final Game game;

    public UserCommon(Game game) {
        this.game = game;
    }

    void rotateShip(Ship shipDrag) {
        List<Ship> ships = game.getShipArrangement().getShips();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        if (shipDrag.getOrientation() == Position.VERTICAL) {
            if (isRotHorizontalPossible(shipDrag)) {
                shipDrag.setOrientation(Position.HORIZONTAL);
            }
        } else {
            if (isRotVerticalPossible(shipDrag)) {
                shipDrag.setOrientation(Position.VERTICAL);
            }
        }
        battleField.resetTiles();
        battleField.putShipsOnTiles(ships);
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
        game.getShipArrangement().setUpdate(true);

        checkProblems();
    }

    boolean isMoveHorizPossible(Ship shipDrag, int yDiff) {
        int length = shipDrag.getOrientation() == Position.HORIZONTAL ? shipDrag.getLength() - 1 : 0;

        return shipDrag.getHead().getColumn() + yDiff >= 0 && shipDrag.getHead().getColumn() + yDiff + length < Dimension.FIELD_WIDTH;
    }

    boolean isMoveVertPossible(Ship shipDrag, int xDiff) {
        int length = shipDrag.getOrientation() == Position.VERTICAL ? shipDrag.getLength() - 1 : 0;

        return shipDrag.getHead().getRow() + xDiff >= 0 && shipDrag.getHead().getRow() + xDiff + length < Dimension.FIELD_HEIGHT;
    }

    void clearShipsOperation() {
        List<Ship> ships = game.getShipArrangement().getShips();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        for (Ship ship : ships) {
            ship.setOperation(ShipStatus.NONE);
        }
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
    }

    private boolean isRotHorizontalPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getColumn() + length < Dimension.FIELD_WIDTH;
    }

    private boolean isRotVerticalPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getRow() + length < Dimension.FIELD_HEIGHT;
    }

    boolean isBetweenMargin(double x, double y) {
        return y >= Dimension.FIELD_LEFT_MARGIN && y < Dimension.CANVAS_WIDTH - Dimension.FIELD_LEFT_MARGIN
                && x >= Dimension.FIELD_TOP_MARGIN && x < Dimension.CANVAS_HEIGHT - Dimension.FIELD_TOP_MARGIN;
    }

    void checkProblems() {
        if (checkCollision() || checkNeighbor()) {
            game.getGameForm().getArrangeForm().getBtnOk().setDisable(true);
            game.getGameForm().getArrangeForm().getLblTitle().setText("Ship overlap or neighbor !");
            game.getGameForm().getArrangeForm().getLblTitle().setTextFill(Color.RED);
        } else {
            game.getGameForm().getArrangeForm().getBtnOk().setDisable(false);
            game.getGameForm().getArrangeForm().getLblTitle().setText("Ship positioned correctly.");
            game.getGameForm().getArrangeForm().getLblTitle().setTextFill(Color.BLUE);
        }
    }

    private boolean checkCollision() {
        return Stream.of(game.getShipArrangement().getArrangeBattleField().getTiles())
                .flatMap(Stream::of)
                .anyMatch(t -> t.getSipsCount() > 1);
    }

    private boolean checkNeighbor() {
        for (int i = 0; i < Dimension.FIELD_WIDTH - 1; i++) {
            for (int j = 0; j < Dimension.FIELD_HEIGHT - 1; j++) {
                if (checkLeftNeighbor(i, j, game.getShipArrangement().getArrangeBattleField().getTiles())) return true;
                if (checkDownNeighbor(i, j, game.getShipArrangement().getArrangeBattleField().getTiles())) return true;
            }
        }

        return false;
    }

    private boolean checkLeftNeighbor(int row, int column, Tile[][] tile) {
        if (tile[row][column + 1].getSipsCount() == 0 || tile[row][column].getSipsCount() == 0) return false;

        return !tile[row][column].getShipCollection().get(0).equals(tile[row][column + 1].getShipCollection().get(0));
    }

    private boolean checkDownNeighbor(int row, int column, Tile[][] tile) {
        if (tile[row + 1][column].getSipsCount() == 0 || tile[row][column].getSipsCount() == 0) return false;

        return !tile[row][column].getShipCollection().get(0).equals(tile[row + 1][column].getShipCollection().get(0));
    }

}
