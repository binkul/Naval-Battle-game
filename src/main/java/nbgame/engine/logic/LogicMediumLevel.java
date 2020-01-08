package nbgame.engine.logic;

import nbgame.game.BattleField;
import nbgame.ship.Point;

public class LogicMediumLevel extends Logic implements ShootLogic {

    public LogicMediumLevel(BattleField battleField) {
        super(battleField);
    }

    @Override
    public Point getNextShoot() {
        int hitCount = getHitPoints().size();
        Point point;

        switch (hitCount) {
            case 0:
                point = randomHit();
                addShoot(point);
                return point;
            case 1:
                point = hitOneAround();
                addShoot(point);
                return point;
            default:
                clearShots();
                return randomHit();
        }
    }
}
