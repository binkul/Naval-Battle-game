package nbgame.engine;

import nbgame.constant.Dimension;
import nbgame.game.Game;
import nbgame.game.Level;
import nbgame.ship.Hit;
import nbgame.ship.Point;
import nbgame.ship.Ship;
import nbgame.ship.Tile;

import java.util.*;

public class ShootLogic implements Logicable {
    private static final Random RANDOM = new Random();
    private static final int MAX_RANDOM_REPEAT = 100;

    private final Game game;
    private List<Point> shootsPoints = new ArrayList<>();
    private Set<Point> avoidsPoints = new HashSet<>();

    public ShootLogic(Game game) {
        this.game = game;
    }

    @Override
    public void buildStrategy(Ship ship, Point shootPoint) {
        if(ship.getHitResult() == Hit.HIT_AND_SINK) {
            updateAvoids(shootPoint);
        } else if (ship.getHitResult() == Hit.HIT) {
            shootsPoints.add(shootPoint);
        }
    }

    @Override
    public Point getNextShoot() {
        Level level = game.getSettings().getLevelOfDifficulty();

        if (level == Level.LOW) {
            return shootRandom();
        } else if (level == Level.MEDIUM) {
            return getMediumLevelShoot();
        } else {
            return getHighLevelShoot();
        }
    }

    private Point getMediumLevelShoot() {
        int logicLevel = shootsPoints.size();

        switch (logicLevel) {
            case 0:
                return shootRandom();
            case 1:
                return shootOneAround();
            default:
                shootsPoints.clear();
                return shootRandom();
        }
    }

    private Point getHighLevelShoot() {
        int logicLevel = shootsPoints.size();

        switch (logicLevel) {
            case 0:
                return shootRandom();
            case 1:
                return shootOneAround();
            default: {
                if (shootsPoints.get(0).getRow() == shootsPoints.get(1).getRow()) {
                    return shootHorizontal();
                } else if (shootsPoints.get(0).getColumn() == shootsPoints.get(1).getColumn()) {
                    return shootVertical();
                } else {
                    clearShoots();
                    return shootRandom();
                }
            }
        }
    }

    private Point shootRandom() {
        boolean repeat = true;
        int repeatCount = 0;
        int row = 0;
        int column = 0;
        Tile tile;

        while (repeat) {
            row = RANDOM.nextInt(Dimension.FIELD_HEIGHT);
            column = RANDOM.nextInt(Dimension.FIELD_WIDTH);
            tile = game.getGamerBattleField().getTiles()[row][column];

            if (game.getSettings().getLevelOfDifficulty() == Level.EXPERT) {
                if (repeatCount > MAX_RANDOM_REPEAT) {
                    repeat = (tile.getHit() != Hit.NONE);
                } else {
                    repeat = (tile.getHit() != Hit.NONE || avoidsPoints.contains(new Point(row, column)));
                }
            } else {
                repeat = (tile.getHit() != Hit.NONE);
            }

            repeatCount++;
        }
        return new Point(row, column);
    }

    private Point shootOneAround() {
        if (shootsPoints.size() == 0) return shootRandom();

        int row = shootsPoints.get(0).getRow();
        int column = shootsPoints.get(0).getColumn();

        if (isShootPossible(row - 1, column)) {
            return new Point(row - 1, column);
        } else if (isShootPossible(row, column - 1)) {
            return new Point(row, column - 1);
        } else if (isShootPossible(row + 1, column)) {
            return new Point(row + 1, column);
        } else if (isShootPossible(row, column + 1)) {
            return new Point(row, column + 1);
        } else {
            clearShoots();
            return shootRandom();
        }
    }

    private Point shootHorizontal() {
        if (shootsPoints.size() == 0) return shootRandom();

        int x = shootsPoints.get(0).getRow();
        int yMin = findYMin();
        int yMax = findYMax();

        if (isShootPossible(x, yMin - 1)) {
            return new Point(x, yMin - 1);
        } else if (isShootPossible(x, yMax + 1)) {
            return new Point(x, yMax + 1);
        } else {
            clearShoots();
            return shootRandom();
        }
    }

    private Point shootVertical() {
        if (shootsPoints.size() == 0) return shootRandom();

        int y = shootsPoints.get(0).getColumn();
        int xMin = findXMin();
        int xMax = findXMax();

        if (isShootPossible(xMin - 1, y)) {
            return new Point(xMin - 1, y);
        } else if (isShootPossible(xMax + 1, y)) {
            return new Point(xMax + 1, y);
        } else {
            clearShoots();
            return shootRandom();
        }
    }

    private void updateAvoids(Point point) {
        int x;
        int y;

        shootsPoints.add(point);

        for (Point shootPoint : shootsPoints) {
            x = shootPoint.getRow();
            y = shootPoint.getColumn();
            if (isShootPossible(x - 1, y)) avoidsPoints.add(new Point(x - 1, y));
            if (isShootPossible(x, y - 1)) avoidsPoints.add(new Point(x, y - 1));
            if (isShootPossible(x + 1, y)) avoidsPoints.add(new Point(x + 1, y));
            if (isShootPossible(x, y + 1)) avoidsPoints.add(new Point(x, y + 1));
        }

        shootsPoints.clear();
    }

    private void clearShoots() {
        shootsPoints.clear();
    }

    private boolean isShootPossible(int row, int column) {
        if (row < 0 || row >= Dimension.FIELD_HEIGHT) return false;
        if (column < 0 || column >= Dimension.FIELD_WIDTH) return false;

        Tile tile = game.getGamerBattleField().getTiles()[row][column];
        return tile.getHit() == Hit.NONE;
    }

    private int findXMin() {
        return shootsPoints.stream()
                .mapToInt(Point::getRow)
                .min()
                .orElse(Dimension.FIELD_HEIGHT - 1);
    }

    private int findXMax() {
        return shootsPoints.stream()
                .mapToInt(Point::getRow)
                .max()
                .orElse(0);
    }

    private int findYMin() {
        return shootsPoints.stream()
                .mapToInt(Point::getColumn)
                .min()
                .orElse(Dimension.FIELD_WIDTH - 1);
    }

    private int findYMax() {
        return shootsPoints.stream()
                .mapToInt(Point::getColumn)
                .max()
                .orElse(0);
   }

}
