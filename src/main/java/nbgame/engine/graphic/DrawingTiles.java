package nbgame.engine.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.engine.PathDriver;
import nbgame.ship.ShipStatus;
import nbgame.ship.Tile;

public class DrawingTiles {
    private final Image blueTileImage;
    private final Image greenTileImage;
    private final Image redTileImage;
    private final Image yellowTileImage;
    private final PathDriver pathDriver;

    public DrawingTiles() {
        pathDriver = new PathDriver();
        blueTileImage = new Image(pathDriver.getPath(FileAccess.BLUE_TILE_PIC_PATH));
        greenTileImage = new Image(pathDriver.getPath(FileAccess.GREEN_TILE_PIC_PATH));
        redTileImage = new Image(pathDriver.getPath(FileAccess.RED_TILE_PIC_PATH));
        yellowTileImage = new Image(pathDriver.getPath(FileAccess.YELLOW_TILE_PIC_PATH));
    }

    void drawAllTiles(Tile[][] tiles, Canvas canvas) {
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

    public PathDriver getPathDriver() {
        return pathDriver;
    }
}
