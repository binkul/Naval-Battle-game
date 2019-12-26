package nbgame.engine;

import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.user.ArrangeMouse;
import nbgame.user.Movable;

import java.util.List;

public class ShipArrangement {
    private final Game game;

    private boolean update;
    private List<Ship> ships;
    private BattleField arrangeBattleField;

    public ShipArrangement(Game game) {
        this.game = game;
        Movable arrangeInterface = new ArrangeMouse(game);
        arrangeBattleField = new BattleField(game, FileAccess.FIELD_PIC_PATH, arrangeInterface);
    }

    public void initBattleField() {
        ships = Init.deepCopyOfShips(game.getGamerShip());
        update = false;
        game.getShipArrangement().getArrangeBattleField().getUserInterface().resetState();
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
