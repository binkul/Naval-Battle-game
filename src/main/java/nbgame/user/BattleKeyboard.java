package nbgame.user;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nbgame.constant.Dimension;
import nbgame.game.Game;
import nbgame.game.Status;
import nbgame.ship.Tile;

public class BattleKeyboard implements Movable {
    private final Game game;

    private int row;
    private int column;

    public BattleKeyboard(Game game) {
        this.game = game;
    }

    @Override
    public void moveOver(Object event) {
        if (!(event instanceof KeyEvent)) return;
        if (game.getStatus() != Status.RUN) return;

        KeyEvent keyEvent = (KeyEvent)event;
        KeyCode key = keyEvent.getCode();

        if (key == KeyCode.LEFT) {
            if (column > 0) column--;
        } else if (key == KeyCode.RIGHT) {
            if (column < Dimension.FIELD_WIDTH - 1) column++;
        } else if (key == KeyCode.UP) {
            if (row > 0) row--;
        } else if (key == KeyCode.DOWN) {
            if (row < Dimension.FIELD_HEIGHT - 1) row++;
        } else if (key == KeyCode.SPACE) {
            pressButton(event);
        }

        int relocateX = column * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
        int relocateY = row * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

        game.getGamerBattleField().getViewFinder().relocate(relocateX, relocateY);
    }

    @Override
    public void pressButton(Object event) {
        if (!(event instanceof KeyEvent)) return;
        if (game.getStatus() != Status.RUN) return;

        Tile tile = game.getComputerBattleField().getTiles()[row][column];
        int shotX = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
        int shotY = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

        game.getRound().run(tile, shotX, shotY);
    }
}
