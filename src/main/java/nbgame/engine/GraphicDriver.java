package nbgame.engine;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.*;

import java.util.List;

public class GraphicDriver {
    private final Image splashImage;
    private final Image hitImage;
    private final PathDriver pathDriver;
    private final Game game;

    public GraphicDriver(Game game) {
        this.game = game;
        pathDriver = new PathDriver();
        splashImage = new Image(pathDriver.getPicPath(FileAccess.MISS_ICO_PATH));
        hitImage = new Image(pathDriver.getPicPath(FileAccess.EXPLODE_ICO_PATH));
    }

    public void repaintAll(List<Ship> ships, Tile[][] tiles, Canvas source) {
        drawAllTiles(tiles, source);
        drawRedLineForNeighbor(tiles, source);
        drawShipsPicOnField(ships, source);
    }

    private void drawAllTiles(Tile[][] tiles, Canvas source) {
        int shipCount;
        ShipStatus shipStatus;

        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                shipCount = tiles[row][column].getSipsCount();

                switch (shipCount) {
                    case 0:
                        drawTile(source, row, column, FileAccess.BLUE_TILE_PIC_PATH);
                        break;
                    case 1:
                        shipStatus = tiles[row][column].getShipCollection().get(0).getOperation();
                        drawTileUnderShip(source, shipStatus, row, column);
                        break;
                    default:
                        drawTile(source, row, column, FileAccess.RED_TILE_PIC_PATH);
                }
            }
        }
    }

    private void drawTileUnderShip(Canvas source, ShipStatus shipStatus, int row, int column) {
        if (shipStatus == ShipStatus.SHIP_MOVE_OVER) {
            drawTile(source, row, column, FileAccess.GREEN_TILE_PIC_PATH);
        } else if(shipStatus == ShipStatus.SHIP_PRESSED) {
            drawTile(source, row, column, FileAccess.YELLOW_TILE_PIC_PATH);
        } else {
            drawTile(source, row, column, FileAccess.BLUE_TILE_PIC_PATH);
        }
    }

    private void drawTile(Canvas source, int row, int column, String picPath) {
        int x = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN - 1;
        int y = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN - 1;
        source.getGraphicsContext2D().drawImage(new Image(pathDriver.getPicPath(picPath)), x, y);
    }

    public void drawRedLineForNeighbor(Tile[][] tiles, Canvas source) {
        source.getGraphicsContext2D().setStroke(Color.RED);
        source.getGraphicsContext2D().setLineWidth(2.0);

        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                if (tiles[row][column].getSipsCount() == 1) {
                    drawUpNeighbor(row, column, tiles, source);
                    drawDownNeighbor(row, column, tiles, source);
                    drawLeftNeighbor(row, column, tiles, source);
                    drawRightNeighbor(row, column, tiles, source);
                }
            }
        }
    }

    public void drawShipsPicOnField(List<Ship> ships, Canvas source) {
        if (ships == null) {
            return;
        }

        for (Ship ship : ships) {
            if (ship.getShipType() == ShipType.FOUR_MAST) {
                drawShip(ship, source, FileAccess.FOUR_MAST_VERT_PIC_PATH, FileAccess.FOUR_MAST_HORIZ_PIC_PATH);
            } else if (ship.getShipType() == ShipType.THREE_MAST) {
                drawShip(ship, source, FileAccess.THREE_MAST_VERT_PIC_PATH, FileAccess.THREE_MAST_HORIZ_PIC_PATH);
            } else if (ship.getShipType() == ShipType.TWO_MAST) {
                drawShip(ship, source, FileAccess.TWO_MAST_VERT_PIC_PATH, FileAccess.TWO_MAST_HORIZ_PIC_PATH);
            } else {
                drawShip(ship, source, FileAccess.ONE_MAST_VERT_PIC_PATH, FileAccess.ONE_MAST_HORIZ_PIC_PATH);
            }
        }
    }

    private void drawShip(Ship ship, Canvas source, String picVert, String picHoriz) {
        int shift = (Dimension.TILE_WIDTH - ship.getWidthPix()) / 2;
        int x = ship.getOrientation() == VertHoriz.VERTICAL ? shift : 1;
        int y = ship.getOrientation() == VertHoriz.VERTICAL ? 1 : shift;
        String shipPath = ship.getOrientation() == VertHoriz.VERTICAL ? picVert : picHoriz;

        y += Dimension.FIELD_LEFT_MARGIN + ship.getHead().getRow() * Dimension.TILE_WIDTH;
        x += Dimension.FIELD_TOP_MARGIN + ship.getHead().getColumn() * Dimension.TILE_HEIGHT;

        source.getGraphicsContext2D().drawImage(new Image(pathDriver.getPicPath(shipPath)), x, y);
    }

    private void drawUpNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int yPos = row * Dimension.TILE_HEIGHT + 18;
        int xPosStart = column * Dimension.TILE_WIDTH + 18;
        int xPosEnd = column * Dimension.TILE_WIDTH + 55;

        if (row == 0) return;

        if (tiles[row - 1][column].getSipsCount() == 1) {
            if (!ship.equals(tiles[row - 1][column].getShipCollection().get(0))) {
                field.getGraphicsContext2D().strokeLine(xPosStart, yPos, xPosEnd, yPos);
            }
        }
    }

    private void drawDownNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int yPos = row * Dimension.TILE_HEIGHT + 55;
        int xPosStart = column * Dimension.TILE_WIDTH + 18;
        int xPosEnd = column * Dimension.TILE_WIDTH + 55;

        if (row >= Dimension.FIELD_HEIGHT - 1) return;

        if (tiles[row + 1][column].getSipsCount() == 1) {
            if (!ship.equals(tiles[row + 1][column].getShipCollection().get(0))) {
                field.getGraphicsContext2D().strokeLine(xPosStart, yPos, xPosEnd, yPos);
            }
        }
    }

    private void drawLeftNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int xPos = column * Dimension.TILE_WIDTH + 18;
        int yPosStart = row * Dimension.TILE_HEIGHT + 18;
        int yPosEnd = row * Dimension.TILE_HEIGHT + 55;

        if (column == 0) return;

        if (tiles[row][column - 1].getSipsCount() == 1) {
            if (!ship.equals(tiles[row][column - 1].getShipCollection().get(0))) {
                field.getGraphicsContext2D().strokeLine(xPos, yPosStart, xPos, yPosEnd);
            }
        }
    }

    private void drawRightNeighbor(int row, int column, Tile[][] tiles, Canvas field) {
        Ship ship = tiles[row][column].getShipCollection().get(0);
        int xPos = column * Dimension.TILE_WIDTH + 55;
        int yPosStart = row * Dimension.TILE_HEIGHT + 18;
        int yPosEnd = row * Dimension.TILE_HEIGHT + 55;

        if (column >= Dimension.FIELD_WIDTH - 1) return;

        if (tiles[row][column + 1].getSipsCount() == 1) {
            if (!ship.equals(tiles[row][column + 1].getShipCollection().get(0))) {
                field.getGraphicsContext2D().strokeLine(xPos, yPosStart, xPos, yPosEnd);
            }
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
        int x;
        int y;
        battleField.repaintBackground();
        drawShipsPicOnField(ships, battleField.getCanvas());

        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
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
    }
}
