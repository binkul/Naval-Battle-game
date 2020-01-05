package nbgame.engine;

import lombok.Getter;
import lombok.Setter;
import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.game.Game;
import nbgame.ship.Ship;
import nbgame.user.ArrangeKeyboard;
import nbgame.user.ArrangeMouse;
import nbgame.user.Movable;

import java.util.List;

@Getter
@Setter
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
        ships = Basic.deepCopyOfShips(game.getGamerShips());
        update = false;
        game.getShipArrangement().getArrangeBattleField().getUserInterface().resetState();
    }

    public void updateGamerShips() {
        game.setGamerShips(ships);
        game.getGamerBattleField().refreshFullBattleField(ships);
    }
}
