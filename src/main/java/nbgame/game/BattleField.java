package nbgame.game;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import nbgame.constant.Dimension;
import nbgame.constant.FileAccess;
import nbgame.engine.PathDriver;
import nbgame.engine.Init;
import nbgame.ship.Hit;
import nbgame.ship.Ship;
import nbgame.ship.Tile;
import nbgame.ship.Position;
import nbgame.user.Movable;

import java.util.List;

public class BattleField extends Canvas {
    private final Game game;
    private final Movable userInterface;
    private final Image background;
    private final PathDriver pathDriver = new PathDriver();

    private Tile[][] tiles;
    private Canvas viewFinder;

    public BattleField(Game game, String backgroundPath, Movable userInterface) {
        super(Dimension.CANVAS_WIDTH, Dimension.CANVAS_HEIGHT);
        this.game = game;
        this.userInterface = userInterface;
        background = new Image(pathDriver.getPath(backgroundPath));
        init();
    }

    public BattleField(Game game, String backgroundPath) {
        this(game, backgroundPath, null);
    }

    private void init() {
        setEvents();
        viewFinder = new Canvas(Dimension.TILE_WIDTH, Dimension.TILE_HEIGHT);
        viewFinder.getGraphicsContext2D().drawImage(new Image(pathDriver.getPath(FileAccess.VIEWFINDER_PIC_PATH)), 0, 0);
        tiles = Init.createField();
        repaintBackground();
    }

    private void setEvents() {
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_DRAGGED, this::enterMouseDragged);
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_RELEASED, this::enterMouseReleased);
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, this::enterMousePressed);
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_ENTERED, this::enterMouseEnter);
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_EXITED, this::enterMouseExited);
        getGraphicsContext2D().getCanvas().addEventHandler(MouseEvent.MOUSE_MOVED, this::enterMouseMoved);
    }

    public void repaintBackground() {
        getGraphicsContext2D().drawImage(background, 0, 0);
    }

    public void resetTiles() {
        for (int i = 0; i < Dimension.FIELD_WIDTH; i++) {
            for (int j = 0; j < Dimension.FIELD_HEIGHT; j++) {
                tiles[i][j].getShipCollection().clear();
                tiles[i][j].setHit(Hit.NONE);
            }
        }
    }

    public void putShipsOnTiles(List<Ship> ships) {
        int row, column;

        for (Ship ship : ships) {
            row = ship.getHead().getRow();
            column = ship.getHead().getColumn();

            for (int i = 0; i < ship.getLength(); i++) {
                tiles[row][column].addShip(ship);
                if (ship.getOrientation() == Position.VERTICAL) {
                    row++;
                } else {
                    column++;
                }
            }
        }
    }

    public void refreshFullBattleField(List<Ship> ships) {
        refreshBattleField(ships);
        game.getGraphicDriver().drawShipsPicOnField(ships, getGraphicsContext2D().getCanvas());
    }

    void refreshBattleField(List<Ship> ships) {
        resetTiles();
        putShipsOnTiles(ships);
        repaintBackground();
    }

    public Movable getUserInterface() {
        return userInterface;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public Canvas getCanvas() {
        return getGraphicsContext2D().getCanvas();
    }

    public Canvas getViewFinder() {
        return viewFinder;
    }

    private void enterMouseExited(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.leaveArea(event);
    }

    private void enterMouseEnter(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.enterArea(event);
    }

    private void enterMouseMoved(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.moveOver(event);
    }

    private void enterMousePressed(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.pressButton(event);
    }

    private void enterMouseReleased(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.releaseButton(event);
    }

    private void enterMouseDragged(MouseEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.moveItem(event);
    }

    public void enterKeyPressed(KeyEvent event) {
        event.consume();
        if (userInterface == null) return;

        userInterface.moveOver(event);
    }

}
