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

    void rotateShip(Ship ship) {
        List<Ship> ships = game.getShipArrangement().getShips();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        if (ship.getOrientation() == Position.VERTICAL_DOWN) {
            rotateFromDown(ship);
        } else if (ship.getOrientation() == Position.HORIZONTAL_RIGHT) {
            rotateFromRight(ship);
        } else if (ship.getOrientation() == Position.VERTICAL_UP) {
            rotateFromUp(ship);
        } else {
            rotateFromLeft(ship);
        }

        battleField.resetTiles();
        battleField.putShipsOnTiles(ships);
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
        game.getShipArrangement().setUpdate(true);

        checkProblems();
    }

    private void rotateFromUp(Ship ship) {
        if (isRotationLeftPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_LEFT);
        } else if (isRotationDownPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_DOWN);
        } else if (isRotationRightPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_RIGHT);
        }
    }

    private void rotateFromDown(Ship ship) {
        if (isRotationRightPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_RIGHT);
        } else if (isRotationUpPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_UP);
        } else if (isRotationLeftPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_LEFT);
        }
    }

    private void rotateFromLeft(Ship ship) {
        if (isRotationDownPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_DOWN);
        } else if (isRotationRightPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_RIGHT);
        } else if (isRotationUpPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_UP);
        }
    }

    private void rotateFromRight(Ship ship) {
        if (isRotationUpPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_UP);
        } else if (isRotationLeftPossible(ship)) {
            ship.setOrientation(Position.HORIZONTAL_LEFT);
        } else if (isRotationDownPossible(ship)) {
            ship.setOrientation(Position.VERTICAL_DOWN);
        }
    }

    boolean isMoveUpPossible(Ship shipDrag, int xDiff) {
        if (shipDrag.getOrientation() == Position.VERTICAL_UP) {
            return shipDrag.getHead().getRow() - (shipDrag.getLength() - 1) + xDiff >= 0;
        } else {
            return shipDrag.getHead().getRow() + xDiff >= 0;
        }
    }

    boolean isMoveDownPossible(Ship shipDrag, int xDiff) {
        if (shipDrag.getOrientation() == Position.VERTICAL_DOWN) {
            return shipDrag.getHead().getRow() + (shipDrag.getLength() - 1) + xDiff < Dimension.FIELD_HEIGHT;
        } else {
            return shipDrag.getHead().getRow() + xDiff < Dimension.FIELD_HEIGHT;
        }
    }

    boolean isMoveLeftPossible(Ship shipDrag, int yDiff) {
        if (shipDrag.getOrientation() == Position.HORIZONTAL_LEFT) {
            return shipDrag.getHead().getColumn() - (shipDrag.getLength() - 1) + yDiff >= 0;
        } else {
            return shipDrag.getHead().getColumn() + yDiff >= 0;
        }
    }

    boolean isMoveRightPossible(Ship shipDrag, int yDiff) {
        if (shipDrag.getOrientation() == Position.HORIZONTAL_RIGHT) {
            return shipDrag.getHead().getColumn() + (shipDrag.getLength() - 1) + yDiff < Dimension.FIELD_WIDTH;
        } else {
            return shipDrag.getHead().getColumn() + yDiff < Dimension.FIELD_WIDTH;
        }
    }

    void clearShipsOperation() {
        List<Ship> ships = game.getShipArrangement().getShips();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        for (Ship ship : ships) {
            ship.setOperation(ShipStatus.NONE);
        }
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
    }

    private boolean isRotationRightPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getColumn() + length < Dimension.FIELD_WIDTH;
    }

    private boolean isRotationLeftPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getColumn() - length >= 0;
    }

    private boolean isRotationDownPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getRow() + length < Dimension.FIELD_HEIGHT;
    }

    private boolean isRotationUpPossible(Ship shipDrag) {
        int length = shipDrag.getLength() - 1;
        return shipDrag.getHead().getRow() - length >= 0;
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
