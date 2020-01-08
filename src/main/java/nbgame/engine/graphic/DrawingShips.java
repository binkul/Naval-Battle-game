package nbgame.engine.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.ship.Position;
import nbgame.ship.Ship;
import nbgame.ship.ShipType;

import java.util.List;

public class DrawingShips extends DrawingNeighbor{

    public DrawingShips() {
        super();
    }

    public void drawShipsPicOnField(List<Ship> ships, Canvas canvas) {
        if (ships == null) {
            return;
        }

        for (Ship ship : ships) {
            drawShip(ship, canvas);
        }
    }

    private void drawShip(Ship ship, Canvas canvas) {
        if (ship.getShipType() == ShipType.FOUR_MAST) {
            drawFourMastShip(ship, canvas);
        } else if (ship.getShipType() == ShipType.THREE_MAST) {
            drawThreeMastShip(ship, canvas);
        } else if (ship.getShipType() == ShipType.TWO_MAST) {
            drawTwoMastShip(ship, canvas);
        } else {
            drawOneMastShip(ship, canvas);
        }
    }

    private void drawFourMastShip(Ship ship, Canvas source) {
        int shiftFromCorner = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = Dimension.FIELD_TOP_MARGIN + ship.getHead().getRow() * Dimension.TILE_HEIGHT;
        int y = Dimension.FIELD_LEFT_MARGIN + ship.getHead().getColumn() * Dimension.TILE_WIDTH;
        String path;

        if (ship.getOrientation() == Position.VERTICAL_DOWN) {
            path = FileAccess.FOUR_MAST_DOWN_PIC_PATH;
            x += 1;
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.VERTICAL_UP) {
            path = FileAccess.FOUR_MAST_UP_PIC_PATH;
            x -= (3 * Dimension.TILE_HEIGHT + 1);
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.HORIZONTAL_RIGHT) {
            path = FileAccess.FOUR_MAST_RIGHT_PIC_PATH;
            x += shiftFromCorner;
            y += 1;
        } else {
            path = FileAccess.FOUR_MAST_LEFT_PIC_PATH;
            x += shiftFromCorner;
            y -= (3 * Dimension.TILE_WIDTH + 1);
        }

        source.getGraphicsContext2D().drawImage(new Image(getPathDriver().getPath(path)), y, x);
    }

    private void drawThreeMastShip(Ship ship, Canvas source) {
        int shiftFromCorner = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = Dimension.FIELD_TOP_MARGIN + ship.getHead().getRow() * Dimension.TILE_HEIGHT;
        int y = Dimension.FIELD_LEFT_MARGIN + ship.getHead().getColumn() * Dimension.TILE_WIDTH;
        String path;

        if (ship.getOrientation() == Position.VERTICAL_DOWN) {
            path = FileAccess.THREE_MAST_DOWN_PIC_PATH;
            x += 1;
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.VERTICAL_UP) {
            path = FileAccess.THREE_MAST_UP_PIC_PATH;
            x -= (2 * Dimension.TILE_HEIGHT + 1);
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.HORIZONTAL_RIGHT) {
            path = FileAccess.THREE_MAST_RIGHT_PIC_PATH;
            x += shiftFromCorner;
            y += 1;
        } else {
            path = FileAccess.THREE_MAST_LEFT_PIC_PATH;
            x += shiftFromCorner;
            y -= (2 * Dimension.TILE_WIDTH + 1);
        }

        source.getGraphicsContext2D().drawImage(new Image(getPathDriver().getPath(path)), y, x);
    }

    private void drawTwoMastShip(Ship ship, Canvas source) {
        int shiftFromCorner = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = Dimension.FIELD_TOP_MARGIN + ship.getHead().getRow() * Dimension.TILE_HEIGHT;
        int y = Dimension.FIELD_LEFT_MARGIN + ship.getHead().getColumn() * Dimension.TILE_WIDTH;
        String path;

        if (ship.getOrientation() == Position.VERTICAL_DOWN) {
            path = FileAccess.TWO_MAST_DOWN_PIC_PATH;
            x += 1;
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.VERTICAL_UP) {
            path = FileAccess.TWO_MAST_UP_PIC_PATH;
            x -= (Dimension.TILE_HEIGHT + 1);
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.HORIZONTAL_RIGHT) {
            path = FileAccess.TWO_MAST_RIGHT_PIC_PATH;
            x += shiftFromCorner;
            y += 1;
        } else {
            path = FileAccess.TWO_MAST_LEFT_PIC_PATH;
            x += shiftFromCorner;
            y -= (Dimension.TILE_WIDTH + 1);
        }

        source.getGraphicsContext2D().drawImage(new Image(getPathDriver().getPath(path)), y, x);
    }

    private void drawOneMastShip(Ship ship, Canvas source) {
        int shiftFromCorner = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = Dimension.FIELD_TOP_MARGIN + ship.getHead().getRow() * Dimension.TILE_HEIGHT;
        int y = Dimension.FIELD_LEFT_MARGIN + ship.getHead().getColumn() * Dimension.TILE_WIDTH;
        String path;

        if (ship.getOrientation() == Position.VERTICAL_DOWN) {
            path = FileAccess.ONE_MAST_DOWN_PIC_PATH;
            x += 1;
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.VERTICAL_UP) {
            path = FileAccess.ONE_MAST_UP_PIC_PATH;
            x += 1;
            y += shiftFromCorner;
        } else if (ship.getOrientation() == Position.HORIZONTAL_RIGHT) {
            path = FileAccess.ONE_MAST_RIGHT_PIC_PATH;
            x += shiftFromCorner;
            y += 1;
        } else {
            path = FileAccess.ONE_MAST_LEFT_PIC_PATH;
            x += shiftFromCorner;
            y += 1;
        }

        source.getGraphicsContext2D().drawImage(new Image(getPathDriver().getPath(path)), y, x);
    }
}
