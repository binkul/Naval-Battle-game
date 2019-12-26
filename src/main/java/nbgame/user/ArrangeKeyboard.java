package nbgame.user;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.ship.ShipStatus;
import nbgame.ship.Tile;

import java.util.List;

public class ArrangeKeyboard implements Movable {
    private final Game game;
    private final UserCommon userCommon;

    private boolean shipTaken;
    private Ship shipDrag;
    private int row;
    private int column;

    public ArrangeKeyboard(Game game) {
        this.game = game;
        this.userCommon = new UserCommon(game);
    }

    @Override
    public void moveOver(Object event) {
        if (!(event instanceof KeyEvent)) return;

        KeyEvent keyEvent = (KeyEvent) event;
        KeyCode key = keyEvent.getCode();

        if (key == KeyCode.LEFT) {
            moveLeft();
        } else if (key == KeyCode.RIGHT) {
            moveRight();
        } else if (key == KeyCode.UP) {
            moveUp();
        } else if (key == KeyCode.DOWN) {
            moveDown();
        } else if (key == KeyCode.ENTER) {
            pressButton(event);
        } else if (key == KeyCode.SPACE) {
            if (shipTaken) {
                userCommon.rotateShip(shipDrag);
            }
        }

        int relocateX = column * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
        int relocateY = row * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

        game.getShipArrangement().getArrangeBattleField().getViewFinder().relocate(relocateX, relocateY);
    }

    @Override
    public void pressButton(Object event) {
        if (!(event instanceof KeyEvent)) return;

        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        Tile tile =  battleField.getTiles()[row][column];

        userCommon.clearShipsOperation();

        if (tile.getSipsCount() == 0) return;

        for (Ship ship : tile.getShipCollection()) {
            if (ship.isHead(row, column)) {

                shipTaken = !shipTaken;
                if (shipTaken) {
                    ship.setOperation(ShipStatus.SHIP_PRESSED);
                } else {
                    ship.setOperation(ShipStatus.SHIP_MOVE_OVER);
                }
                shipDrag = ship;
                game.getGraphicDriver().repaintAll(game.getShipArrangement().getShips(), battleField.getTiles(), battleField.getCanvas());
                return;
            }
        }
    }

    @Override
    public void resetState() {
        shipTaken = false;
        shipDrag = null;
        row = 0;
        column = 0;

        game.getShipArrangement().getArrangeBattleField().refreshFullBattleField(game.getShipArrangement().getShips());
        userCommon.checkProblems();
        moveCursor();
    }

    private void moveLeft() {
        if (shipTaken) {
            if (userCommon.isMoveHorizPossible(shipDrag,-1)) {
                dragShip(0, -1);
            }
        } else {
            if (column > 0) column--;
            moveCursor();
        }
    }

    private void moveRight() {
        if (shipTaken) {
            if (userCommon.isMoveHorizPossible(shipDrag,1)) {
                dragShip(0, 1);
            }
        } else {
            if (column < Dimension.FIELD_WIDTH - 1) column++;
            moveCursor();
        }
    }

    private void moveUp() {
        if (shipTaken) {
            if (userCommon.isMoveVertPossible(shipDrag,-1)) {
                dragShip(-1, 0);
            }
        } else {
            if (row > 0) row--;
            moveCursor();
        }

    }

    private void moveDown() {
        if (shipTaken) {
            if (userCommon.isMoveVertPossible(shipDrag,1)) {
                dragShip(1, 0);
            }
        } else {
            if (row < Dimension.FIELD_HEIGHT - 1) row++;
            moveCursor();
        }

    }

    private void moveCursor() {
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        Tile tile = battleField.getTiles()[row][column];
        userCommon.clearShipsOperation();

        if (tile.getSipsCount() == 0) return;

        for (Ship ship : tile.getShipCollection()) {
            if (ship.isHead(row, column)) {
                ship.setOperation(ShipStatus.SHIP_MOVE_OVER);
            }
        }
        game.getGraphicDriver().repaintAll(game.getShipArrangement().getShips(), battleField.getTiles(), battleField.getCanvas());
    }

    private void dragShip(int xFactor, int yFactor) {
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        List<Ship> ships = game.getShipArrangement().getShips();

        shipDrag.getHead().setRow(shipDrag.getHead().getRow() + xFactor);
        shipDrag.getHead().setColumn(shipDrag.getHead().getColumn() + yFactor);
        row += xFactor;
        column += yFactor;

        battleField.resetTiles();
        battleField.putShipsOnTiles(ships);
        game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());

        game.getShipArrangement().setUpdate(true);

        int relocateX = column * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
        int relocateY = row * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

        game.getShipArrangement().getArrangeBattleField().getViewFinder().relocate(relocateX, relocateY);
        userCommon.checkProblems();
    }
}
