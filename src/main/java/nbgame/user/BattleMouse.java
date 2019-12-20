package nbgame.user;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import nbgame.constant.Dimension;
import nbgame.game.Game;
import nbgame.game.Status;
import nbgame.ship.Tile;

public class BattleMouse implements Movable {
    private final Game game;
    private final UserCommon userCommon;

    public BattleMouse(Game game) {
        this.game = game;
        this.userCommon = new UserCommon(game);
    }

    @Override
    public void moveOver(Object event) {
        if (!(event instanceof MouseEvent)) return;
        if (game.getStatus() != Status.RUN) return;

        MouseEvent mouseEvent = (MouseEvent)event;
        int x = (int)mouseEvent.getX();
        int y = (int)mouseEvent.getY();
        if (userCommon.isBetweenMargin(x, y)) {
            int row = (x - Dimension.FIELD_TOP_MARGIN) / Dimension.TILE_HEIGHT;
            int column = (y - Dimension.FIELD_LEFT_MARGIN) / Dimension.TILE_WIDTH;
            int relocateX = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
            int relocateY = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;

            game.getGamerBattleField().getViewFinder().relocate(relocateX, relocateY);
        }
    }

    @Override
    public void pressButton(Object event) {
        if (!(event instanceof MouseEvent)) return;
        if (game.getStatus() != Status.RUN) return;

        MouseEvent mouseEvent = (MouseEvent)event;
        int x = (int)mouseEvent.getY();
        int y = (int)mouseEvent.getX();

        if (userCommon.isBetweenMargin(x, y)) {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                int row = (x - Dimension.FIELD_TOP_MARGIN) / Dimension.TILE_HEIGHT;
                int column = (y - Dimension.FIELD_LEFT_MARGIN) / Dimension.TILE_WIDTH;
                int shotX = row * Dimension.TILE_HEIGHT + Dimension.FIELD_TOP_MARGIN;
                int shotY = column * Dimension.TILE_WIDTH + Dimension.FIELD_LEFT_MARGIN;
                Tile tile = game.getComputerBattleField().getTiles()[row][column];

                game.getRound().run(tile, shotX, shotY);
            }
        }
    }
}
