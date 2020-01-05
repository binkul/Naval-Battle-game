package nbgame.engine.logic;

import lombok.Getter;
import nbgame.constant.Dimension;
import nbgame.game.BattleField;
import nbgame.ship.Hit;
import nbgame.ship.Point;
import nbgame.ship.Tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class Logic {
    private static final Random RANDOM = new Random();

    private final BattleField battleField;
    private List<Point> hitPoints;

    public Logic(BattleField battleField) {
        this.battleField = battleField;
        hitPoints = new ArrayList<>();
    }

    Point hitOneAround() {
        int row = hitPoints.get(0).getRow();
        int column = hitPoints.get(0).getColumn();

        if (isShootPossible(row - 1, column)) {
            return new Point(row - 1, column);
        } else if (isShootPossible(row, column - 1)) {
            return new Point(row, column - 1);
        } else if (isShootPossible(row + 1, column)) {
            return new Point(row + 1, column);
        } else if (isShootPossible(row, column + 1)) {
            return new Point(row, column + 1);
        } else {
            hitPoints.clear();
            return randomHit();
        }
    }

    boolean isShootPossible(int row, int column) {
        if (borderOutOfRange(row, column)) return false;

        Tile tile = battleField.getTiles()[row][column];
        return tile.getHit() == Hit.NONE;
    }

    Point randomHit() {
        int row, column;

        while (true) {
            row = generateRow();
            column = generateColumn();
            if (!isFired(battleField.getTiles()[row][column])) {
                return new Point(row, column);
            }
        }
    }

    private int generateRow() {
        return RANDOM.nextInt(Dimension.FIELD_HEIGHT);
    }

    private int generateColumn() {
        return RANDOM.nextInt(Dimension.FIELD_WIDTH);
    }

    public void addShoot(Point shoot) {
        Tile tile = battleField.getTiles()[shoot.getRow()][shoot.getColumn()];
        if (isHit(tile)) {
            if (tile.getShipCollection().get(0).getHit() < tile.getShipCollection().get(0).getLength() - 1) {
                hitPoints.add(shoot);
            } else {
                hitPoints.clear();
            }
        }
    }

    void clearShots() {
        hitPoints.clear();
    }

    int findXMin() {
        return hitPoints.stream()
                .mapToInt(Point::getRow)
                .min()
                .orElse(Dimension.FIELD_HEIGHT - 1);
    }

    int findXMax() {
        return hitPoints.stream()
                .mapToInt(Point::getRow)
                .max()
                .orElse(0);
    }

    int findYMin() {
        return hitPoints.stream()
                .mapToInt(Point::getColumn)
                .min()
                .orElse(Dimension.FIELD_WIDTH - 1);
    }

    int findYMax() {
        return hitPoints.stream()
                .mapToInt(Point::getColumn)
                .max()
                .orElse(0);
    }

    private boolean isFired(Tile tile) {
        return tile.getHit() != Hit.NONE;
    }

    boolean isHit(Tile tile) {
        return tile.getSipsCount() > 0;
    }

    boolean borderOutOfRange(int row, int column) {
        return row < 0 || row >= Dimension.FIELD_HEIGHT || column < 0 || column >= Dimension.FIELD_WIDTH;
    }
}
