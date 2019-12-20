package nbgame.engine;

import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.user.ArrangeKeyboard;
import nbgame.user.ArrangeMouse;
import nbgame.user.Movable;

import java.util.List;

public class ShipArrangement {
    private final Game game;

    private boolean update;
    private List<Ship> ships;
    private Movable  arrangeInterface;
    private BattleField arrangeBattleField;

    public ShipArrangement(Game game) {
        this.game = game;
        arrangeInterface = new ArrangeKeyboard(game); //ArrangeMouse(game);
        arrangeBattleField = new BattleField(game, FileAccess.FIELD_PIC_PATH, arrangeInterface);
    }

    public void initBattleField(boolean newArrange) {

        if (newArrange) {
            ships = Init.createShips(game.getSettings());
        } else {
            ships = Init.deepCopyOfShips(game.getGamerShip()); // game.getGamerShip();
        }

        update = false;
        arrangeBattleField.refreshFullBattleField(ships);
        game.getGraphicDriver().drawRedLineForNeighbor(arrangeBattleField.getTiles(), arrangeBattleField.getCanvas());
    }

    public List<Ship> getShips() {
        return ships;
    }

    public BattleField getArrangeBattleField() {
        return arrangeBattleField;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public boolean isUpdate() {
        return update;
    }

    public void updateGamerShips() {
        game.setGamerShips(ships);
        game.getGamerBattleField().refreshFullBattleField(ships);
    }
}
