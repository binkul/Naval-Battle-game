package nbgame.user;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.ship.ShipStatus;
import nbgame.ship.Tile;

import java.util.List;

public class ArrangeMouse implements Movable {
    private final Game game;
    private final UserCommon userCommon;

    private boolean mouseEntered;
    private boolean mousePressed;
    private int xMoveOld;
    private int yMoveOld;
    private int xDragOld;
    private int yDragOld;
    private Ship shipDrag;

    public ArrangeMouse(Game game) {
        this.game = game;
        userCommon = new UserCommon(game);
    }

    @Override
    public void enterArea(Object event) {
        if (!(event instanceof MouseEvent)) return;

        mouseEntered = true;
    }

    @Override
    public void leaveArea(Object event) {
        if (!(event instanceof MouseEvent)) return;

        mousePressed = false;
        mouseEntered = false;
        userCommon.clearShipsOperation();
    }

    @Override
    public void moveOver(Object event) {
        if (!(event instanceof MouseEvent)) return;

        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        MouseEvent mouseEvent = (MouseEvent)event;
        int xMove;
        int yMove;

        if (userCommon.isBetweenMargin(mouseEvent.getX(), mouseEvent.getY())) {
            xMove = ((int)mouseEvent.getY() - Dimension.FIELD_LEFT_MARGIN) / Dimension.TILE_WIDTH;
            yMove = ((int)mouseEvent.getX() - Dimension.FIELD_TOP_MARGIN) / Dimension.TILE_HEIGHT;

            if (xMove != xMoveOld || yMove != yMoveOld || mouseEntered) {
                Tile tile = battleField.getTiles()[xMove][yMove];

                userCommon.clearShipsOperation();

                xMoveOld = xMove;
                yMoveOld = yMove;
                mouseEntered = false;

                if (tile.getSipsCount() == 0) return;

                for (Ship ship : tile.getShipCollection()) {
                    if (ship.isHead(xMove, yMove)) {
                        ship.setOperation(ShipStatus.SHIP_MOVE_OVER);
                    }
                }
                game.getGraphicDriver().repaintAll(game.getShipArrangement().getShips(), battleField.getTiles(), battleField.getCanvas());
            }
        }

    }

    @Override
    public void pressButton(Object event) {
        if (!(event instanceof MouseEvent)) return;
        MouseEvent mouseEvent = (MouseEvent)event;

        if (userCommon.isBetweenMargin(mouseEvent.getX(), mouseEvent.getY())) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                mousePressed = true;
                takeShipToDrag(mouseEvent);
            } else if (mouseEvent.getButton() == MouseButton.SECONDARY && mousePressed) {
                userCommon.rotateShip(shipDrag);
            }
        }
    }

    @Override
    public void releaseButton(Object event) {
        if (!(event instanceof MouseEvent)) return;
        MouseEvent mouseEvent = (MouseEvent)event;

        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            mousePressed = true;
            userCommon.clearShipsOperation();
        }
    }

    @Override
    public void moveItem(Object event) {
        if (!(event instanceof MouseEvent)) return;
        MouseEvent mouseEvent = (MouseEvent)event;
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        List<Ship> ships = game.getShipArrangement().getShips();
        int xDrag;
        int yDrag;
        int xDiff;
        int yDiff;

        if (userCommon.isBetweenMargin(mouseEvent.getX(), mouseEvent.getY()) && mousePressed) {
            xDrag = ((int)mouseEvent.getY() - Dimension.FIELD_LEFT_MARGIN) / Dimension.TILE_WIDTH;
            yDrag = ((int)mouseEvent.getX() - Dimension.FIELD_TOP_MARGIN) / Dimension.TILE_HEIGHT;
            xDiff = xDrag - xDragOld;
            yDiff = yDrag - yDragOld;

            if (userCommon.isMoveVertPossible(shipDrag, xDiff)) {
                shipDrag.getHead().setRow(shipDrag.getHead().getRow() + xDiff);
            }

            if (userCommon.isMoveHorizPossible(shipDrag, yDiff)) {
                shipDrag.getHead().setColumn((shipDrag.getHead().getColumn() + yDiff));
            }

            xDragOld = xDrag;
            yDragOld = yDrag;

            battleField.resetTiles();
            battleField.putShipsOnTiles(ships);
            game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
            game.getShipArrangement().setUpdate(true);

            userCommon.checkProblems();
        }
    }

    @Override
    public void resetState() {
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();

        shipDrag = null;
        game.getShipArrangement().getArrangeBattleField().refreshFullBattleField(game.getShipArrangement().getShips());
        game.getGraphicDriver().drawRedLineForNeighbor(battleField.getTiles(), battleField.getCanvas());
        userCommon.checkProblems();
    }

    private void takeShipToDrag(MouseEvent event) {
        List<Ship> ships = game.getShipArrangement().getShips();
        BattleField battleField = game.getShipArrangement().getArrangeBattleField();
        int xDrag = ((int)event.getY() - Dimension.FIELD_LEFT_MARGIN) / Dimension.TILE_WIDTH;
        int yDrag = ((int)event.getX() - Dimension.FIELD_TOP_MARGIN) / Dimension.TILE_HEIGHT;
        Tile tile =  battleField.getTiles()[xDrag][yDrag];

        userCommon.clearShipsOperation();

        if (tile.getSipsCount() == 0) return;

        for (Ship ship : tile.getShipCollection()) {
            if (ship.isHead(xDrag, yDrag)) {
                ship.setOperation(ShipStatus.SHIP_PRESSED);
                game.getGraphicDriver().repaintAll(ships, battleField.getTiles(), battleField.getCanvas());
                xDragOld = xDrag;
                yDragOld = yDrag;
                shipDrag = ship;
                return;
            }
        }
    }
}
