package nbgame.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.ship.*;

import java.util.List;

public class GraphicDriver {
    private final Image splashImage;
    private final Image hitImage;
    private final Image blueTileImage;
    private final Image greenTileImage;
    private final Image redTileImage;
    private final Image yellowTileImage;
    private final PathDriver pathDriver;

    public GraphicDriver() {
        pathDriver = new PathDriver();
        splashImage = new Image(pathDriver.getPath(FileAccess.MISS_ICO_PATH));
        hitImage = new Image(pathDriver.getPath(FileAccess.EXPLODE_ICO_PATH));
        blueTileImage = new Image(pathDriver.getPath(FileAccess.BLUE_TILE_PIC_PATH));
        greenTileImage = new Image(pathDriver.getPath(FileAccess.GREEN_TILE_PIC_PATH));
        redTileImage = new Image(pathDriver.getPath(FileAccess.RED_TILE_PIC_PATH));
        yellowTileImage = new Image(pathDriver.getPath(FileAccess.YELLOW_TILE_PIC_PATH));
    }

    public void repaintAll(List<Ship> ships, Tile[][] tiles, Canvas canvas) {
        drawAllTiles(tiles, canvas);
        drawRedLineForNeighbor(tiles, canvas);
        drawShipsPicOnField(ships, canvas);
    }

    private void drawAllTiles(Tile[][] tiles, Canvas canvas) {
        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                drawTile(tiles[row][column], row, column, canvas);
            }
        }
    }

    private void drawTile(Tile tile, int row, int column, Canvas canvas) {
        int x = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN - 1;
        int y = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN - 1;
        int shipCount = tile.getSipsCount();

        switch (shipCount) {
            case 0:
                canvas.getGraphicsContext2D().drawImage(blueTileImage, x, y);
                break;
            case 1:
                ShipStatus shipStatus = tile.getShipCollection().get(0).getOperation();
                if (shipStatus == ShipStatus.SHIP_MOVE_OVER) {
                    canvas.getGraphicsContext2D().drawImage(greenTileImage, x, y);
                } else if(shipStatus == ShipStatus.SHIP_PRESSED) {
                    canvas.getGraphicsContext2D().drawImage(yellowTileImage, x, y);
                } else {
                    canvas.getGraphicsContext2D().drawImage(blueTileImage, x, y);
                }
                break;
            default:
                canvas.getGraphicsContext2D().drawImage(redTileImage, x, y);
        }
    }

    public void drawRedLineForNeighbor(Tile[][] tiles, Canvas canvas) {
        canvas.getGraphicsContext2D().setStroke(Color.RED);
        canvas.getGraphicsContext2D().setLineWidth(2.0);

        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                drawNeighbors(tiles, row, column, canvas);
            }
        }
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
            drawShipPicture(ship, canvas, FileAccess.FOUR_MAST_VERT_PIC_PATH, FileAccess.FOUR_MAST_HORIZ_PIC_PATH);
        } else if (ship.getShipType() == ShipType.THREE_MAST) {
            drawShipPicture(ship, canvas, FileAccess.THREE_MAST_VERT_PIC_PATH, FileAccess.THREE_MAST_HORIZ_PIC_PATH);
        } else if (ship.getShipType() == ShipType.TWO_MAST) {
            drawShipPicture(ship, canvas, FileAccess.TWO_MAST_VERT_PIC_PATH, FileAccess.TWO_MAST_HORIZ_PIC_PATH);
        } else {
            drawShipPicture(ship, canvas, FileAccess.ONE_MAST_VERT_PIC_PATH, FileAccess.ONE_MAST_HORIZ_PIC_PATH);
        }
    }

    private void drawShipPicture(Ship ship, Canvas source, String picVert, String picHoriz) {
        int shift = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = ship.getOrientation() == Position.VERTICAL ? shift : 1;
        int y = ship.getOrientation() == Position.VERTICAL ? 1 : shift;
        String shipPath = ship.getOrientation() == Position.VERTICAL ? picVert : picHoriz;

        y += Dimension.FIELD_LEFT_MARGIN + ship.getHead().getRow() * Dimension.TILE_WIDTH;
        x += Dimension.FIELD_TOP_MARGIN + ship.getHead().getColumn() * Dimension.TILE_HEIGHT;

        source.getGraphicsContext2D().drawImage(new Image(pathDriver.getPath(shipPath)), x, y);
    }

    private void drawNeighbors(Tile[][] tiles, int row, int column, Canvas canvas) {
        if (tiles[row][column].getSipsCount() == 1) {
            drawUpNeighbor(row, column, tiles, canvas);
            drawDownNeighbor(row, column, tiles, canvas);
            drawLeftNeighbor(row, column, tiles, canvas);
            drawRightNeighbor(row, column, tiles, canvas);
        }
    }

    private boolean shouldStrokeLine(Tile[][] tiles, Ship ship, int row, int column) {
        return tiles[row][column].getSipsCount() == 1 && ! ship.equals(tiles[row][column].getShipCollection().get(0));
    }

    private void drawUpNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int yPos = row * Dimension.TILE_HEIGHT + 18;
        int xPosStart = column * Dimension.TILE_WIDTH + 18;
        int xPosEnd = column * Dimension.TILE_WIDTH + 55;

        if (row == 0) return;
        if (shouldStrokeLine(tiles, ship, row - 1, column)) {
            field.getGraphicsContext2D().strokeLine(xPosStart, yPos, xPosEnd, yPos);
        }
    }

    private void drawDownNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int yPos = row * Dimension.TILE_HEIGHT + 55;
        int xPosStart = column * Dimension.TILE_WIDTH + 18;
        int xPosEnd = column * Dimension.TILE_WIDTH + 55;

        if (row >= Dimension.FIELD_HEIGHT - 1) return;
        if (shouldStrokeLine(tiles, ship, row + 1, column)) {
            field.getGraphicsContext2D().strokeLine(xPosStart, yPos, xPosEnd, yPos);
        }
    }

    private void drawLeftNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int xPos = column * Dimension.TILE_WIDTH + 18;
        int yPosStart = row * Dimension.TILE_HEIGHT + 18;
        int yPosEnd = row * Dimension.TILE_HEIGHT + 55;

        if (column == 0) return;
        if (shouldStrokeLine(tiles, ship, row, column - 1)) {
            field.getGraphicsContext2D().strokeLine(xPos, yPosStart, xPos, yPosEnd);
        }
    }

    private void drawRightNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int xPos = column * Dimension.TILE_WIDTH + 55;
        int yPosStart = row * Dimension.TILE_HEIGHT + 18;
        int yPosEnd = row * Dimension.TILE_HEIGHT + 55;

        if (column >= Dimension.FIELD_WIDTH - 1) return;
        if (shouldStrokeLine(tiles, ship, row, column + 1)) {
            field.getGraphicsContext2D().strokeLine(xPos, yPosStart, xPos, yPosEnd);
        }
    }

    public void drawExplosion(Canvas source, int x, int y, Hit hitType) {
        if (hitType == Hit.NONE) {
            source.getGraphicsContext2D().drawImage(splashImage, y, x, Dimension.TILE_HEIGHT, Dimension.TILE_WIDTH);
        } else {
            source.getGraphicsContext2D().drawImage(hitImage, y, x, Dimension.TILE_HEIGHT, Dimension.TILE_WIDTH);
        }
    }

    public void drawAllAfterBattle(BattleField battleField, List<Ship> ships) {
        battleField.repaintBackground();
        drawShipsPicOnField(ships, battleField.getCanvas());

        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                drawHitAfterBattle(battleField, row, column);
            }
        }
    }

    private void drawHitAfterBattle(BattleField battleField, int row, int column) {
        int x, y;

        Tile tile = battleField.getTiles()[row][column];
        if (tile.getHit() == Hit.HIT) {
            x = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
            y = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;
            if (tile.getSipsCount() > 0) {
                drawExplosion(battleField.getCanvas(), x, y, Hit.HIT);
            } else {
                drawExplosion(battleField.getCanvas(), x, y, Hit.NONE);
            }
        }
    }
}
