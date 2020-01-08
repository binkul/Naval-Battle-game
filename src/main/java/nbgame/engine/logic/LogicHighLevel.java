package nbgame.engine.logic;

import nbgame.game.BattleField;
import nbgame.ship.Point;

public class LogicHighLevel extends Logic implements SinkLogic {

    public LogicHighLevel(BattleField battleField) {
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
                return destroyShip();
        }
    }

    @Override
    public Point destroyShip() {
        Point point;

        if (getHitPoints().get(0).getRow() == getHitPoints().get(1).getRow()) {
            point = hitShipHorizontal();
            addShoot(point);
        } else if (getHitPoints().get(0).getColumn() == getHitPoints().get(1).getColumn()) {
            point = hitShipVertical();
            addShoot(point);
        } else {
            clearShots();
            point = randomHit();
        }

        return point;
    }

    @Override
    public Point hitShipHorizontal() {
        int x = getHitPoints().get(0).getRow();
        int yMin = findYMin();
        int yMax = findYMax();

        if (isShootPossible(x, yMin - 1)) {
            return new Point(x, yMin - 1);
        } else if (isShootPossible(x, yMax + 1)) {
            return new Point(x, yMax + 1);
        } else {
            clearShots();
            return randomHit();
        }
    }

    @Override
    public Point hitShipVertical() {
        int y = getHitPoints().get(0).getColumn();
        int xMin = findXMin();
        int xMax = findXMax();

        if (isShootPossible(xMin - 1, y)) {
            return new Point(xMin - 1, y);
        } else if (isShootPossible(xMax + 1, y)) {
            return new Point(xMax + 1, y);
        } else {
            clearShots();
            return randomHit();
        }
    }

}
