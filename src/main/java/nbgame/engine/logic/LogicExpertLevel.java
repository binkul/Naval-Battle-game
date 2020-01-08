package nbgame.engine.logic;

import lombok.Getter;
import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.ship.Point;
import nbgame.ship.Tile;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Getter
public class LogicExpertLevel extends Logic implements SinkLogic {
    private static final Random RANDOM = new Random();
    private final BattleField battleField;
    private List<Point> shootsRange;

    public LogicExpertLevel(BattleField battleField) {
        super(battleField);
        this.battleField = battleField;
        shootsRange = buildShootsRange();
    }

    @Override
    public Point getNextShoot() {
        int hitCount = getHitPoints().size();
        Point point;

        switch (hitCount) {
            case 0:
                point = randomFromRange();
                addShoot(point);
                updateStrategy(point);
                return point;
            case 1:
                point = hitOneAround();
                addShoot(point);
                updateStrategy(point);
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
            updateStrategy(point);
        } else if (getHitPoints().get(0).getColumn() == getHitPoints().get(1).getColumn()) {
            point = hitShipVertical();
            addShoot(point);
            updateStrategy(point);
        } else {
            clearShots();
            point = randomFromRange();
            updateStrategy(point);
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
            return randomFromRange();
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
            return randomFromRange();
        }
    }

    private List<Point> buildShootsRange() {
        List<Point> range = new LinkedList<>();
        for (int row = 0; row < Dimension.FIELD_HEIGHT; row++) {
            for (int column = 0; column < Dimension.FIELD_WIDTH; column++) {
                range.add(new Point(row, column));
            }
        }
        return range;
    }

    private void updateStrategy(Point point) {
        Tile tile = battleField.getTiles()[point.getRow()][point.getColumn()];
        shootsRange.remove(point);
        if (isHit(tile)) {
            if (!borderOutOfRange(point.getRow() - 1, point.getColumn())) {
                shootsRange.remove(new Point(point.getRow() - 1, point.getColumn()));
            }
            if (!borderOutOfRange(point.getRow() + 1, point.getColumn())) {
                shootsRange.remove(new Point(point.getRow() + 1, point.getColumn()));
            }
            if (!borderOutOfRange(point.getRow(), point.getColumn() - 1)) {
                shootsRange.remove(new Point(point.getRow(), point.getColumn() - 1));
            }
            if (!borderOutOfRange(point.getRow(), point.getColumn() + 1)) {
                shootsRange.remove(new Point(point.getRow(), point.getColumn() + 1));
            }
        }
//        } else {
//            shootsRange.remove(point);
//       }
    }

    private Point randomFromRange() {
        if (shootsRange.size() > 0) {
            int number = RANDOM.nextInt(shootsRange.size());
            return shootsRange.get(number);
        } else {
            return randomHit();
        }
    }
}
