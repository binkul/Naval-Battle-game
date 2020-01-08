package nbgame.engine.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import nbgame.constant.Dimension;
import nbgame.ship.Ship;
import nbgame.ship.Tile;

public class DrawingNeighbor extends DrawingTiles{

    public DrawingNeighbor() {
        super();
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

    private void drawNeighbors(Tile[][] tiles, int row, int column, Canvas canvas) {
        if (tiles[row][column].getSipsCount() == 1) {
            drawUpNeighbor(row, column, tiles, canvas);
            drawDownNeighbor(row, column, tiles, canvas);
            drawLeftNeighbor(row, column, tiles, canvas);
            drawRightNeighbor(row, column, tiles, canvas);
        }
    }

    private boolean shouldStrokeLine(Tile[][] tiles, Ship ship, int row, int column) {
        return tiles[row][column].getSipsCount() == 1 && !ship.equals(tiles[row][column].getShipCollection().get(0));
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
}
