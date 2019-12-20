package nbgame.user;

import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.ship.ShipStatus;
import nbgame.ship.VertHoriz;

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

        if (shipDrag.getOrientation() == VertHoriz.VERTICAL) {
            if (isRotHorizontalPossible(shipDrag)) {
                shipDrag.setOrientation(VertHoriz.HORIZONTAL);
            }
        } else {
            if (isRotVerticalPossible(shipDrag)) {
                shipDrag.setOrientation(VertHoriz.VERTICAL);
            }
        }
        battleField.resetTiles();
        battleField.putShipsOnTiles(ships);
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
        game.getShipArrangement().setUpdate(true);

        game.getGameForm().getArrangeForm().getBtnOk().setDisable(checkCollision());

    }

    boolean isMoveHorizPossible(Ship shipDrag, int yDiff) {
        int length = shipDrag.getOrientation() == VertHoriz.HORIZONTAL ? shipDrag.getLength() - 1 : 0;

        return shipDrag.getHead().getColumn() + yDiff >= 0 && shipDrag.getHead().getColumn() + yDiff + length < Dimension.FIELD_WIDTH;
    }

    boolean isMoveVertPossible(Ship shipDrag, int xDiff) {
        int length = shipDrag.getOrientation() == VertHoriz.VERTICAL ? shipDrag.getLength() - 1 : 0;

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

    boolean checkCollision() {
        return Stream.of(game.getShipArrangement().getArrangeBattleField().getTiles())
                .flatMap(Stream::of)
                .anyMatch(t -> t.getSipsCount() > 1);
    }


}
