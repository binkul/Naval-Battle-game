package nbgame.engine.graphic;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.ship.Hit;
import nbgame.ship.Ship;
import nbgame.ship.Tile;

import java.util.List;

public class GraphicDriver extends DrawingShips{
    private final Image splashImage;
    private final Image hitImage;

    public GraphicDriver() {
        super();
        splashImage = new Image(getPathDriver().getPath(FileAccess.MISS_ICO_PATH));
        hitImage = new Image(getPathDriver().getPath(FileAccess.EXPLODE_ICO_PATH));
    }

    public void repaintAll(List<Ship> ships, Tile[][] tiles, Canvas canvas) {
        drawAllTiles(tiles, canvas);
        drawRedLineForNeighbor(tiles, canvas);
        drawShipsPicOnField(ships, canvas);
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

    public void drawExplosion(Canvas source, int x, int y, Hit hitType) {
        if (hitType == Hit.NONE) {
            source.getGraphicsContext2D().drawImage(splashImage, y, x, Dimension.TILE_HEIGHT, Dimension.TILE_WIDTH);
        } else {
            source.getGraphicsContext2D().drawImage(hitImage, y, x, Dimension.TILE_HEIGHT, Dimension.TILE_WIDTH);
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
