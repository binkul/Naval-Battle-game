package nbgame.engine.logic;

import nbgame.game.BattleField;
import nbgame.ship.Point;

public class LogicLowLevel extends Logic implements ShootLogic {

    public LogicLowLevel(BattleField battleField) {
        super(battleField);
    }

    @Override
    public Point getNextShoot() {
        return randomHit();
    }
}
