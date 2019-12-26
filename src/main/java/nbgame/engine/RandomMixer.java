package nbgame.engine;

import nbgame.constant.Dimension;
import nbgame.game.Settings;
import nbgame.ship.Ship;
import nbgame.ship.ShipType;
import nbgame.ship.Tile;
import nbgame.ship.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMixer {
    private static final Random RANDOM = new Random();

    public static List<Ship> randomShip(Settings settings) {
        List<Ship> ships = new ArrayList<>();
        Tile[][] tiles = Init.createField();

        generateShip(settings.getFourMastCount(), ShipType.FOUR_MAST, tiles, ships);
        generateShip(settings.getThreeMastCount(), ShipType.THREE_MAST, tiles, ships);
        generateShip(settings.getTwoMastCount(), ShipType.TWO_MAST, tiles, ships);
        generateShip(settings.getOneMastCount(), ShipType.ONE_MAST, tiles, ships);

        return ships;
    }

    private static void generateShip(int count, ShipType shipType, Tile[][] tiles, List<Ship> ships) {
        Ship tmpShip;
        int id = 1;

        for (int i = 0; i < count; i++) {
            tmpShip = ship(id, shipType, tiles);
            ships.add(tmpShip);
            Init.putShipOnField(tmpShip, tiles);
            id++;
        }
    }

    private static Ship ship (int id, ShipType shipType, Tile[][] tiles) {
        Position orientation = null;
        int row = 0;
        int column = 0;
        int neighbor = 1;

        while (neighbor > 0) {
            orientation = getOrientation() ? Position.VERTICAL : Position.HORIZONTAL;
            row = getRow(orientation, shipType);
            column = getColumn(orientation, shipType);

            if (orientation == Position.VERTICAL) {
                neighbor = checkVertNeighbor(row, column, shipType.getShipLength(), tiles);
            } else {
                neighbor = checkHorizNeighbor(row, column, shipType.getShipLength(), tiles);
            }
        }

        return new Ship(id, row, column, shipType, orientation);
    }

    private static int checkVertNeighbor(int row, int column, int length, Tile[][] tiles) {
        int result = 0;

        for (int i = 0; i < length; i++) {
            result += checkInside(row + i, column, tiles) +
                    checkUp(row + i, column, tiles) +
                    checkDown(row + i, column, tiles) +
                    checkLeft(row + i, column, tiles) +
                    checkRight(row + i, column, tiles);
        }

        return result;
    }

    private static int checkHorizNeighbor(int row, int column, int length, Tile[][] tiles) {
        int result = 0;

        for (int i = 0; i < length; i++) {
            result += checkInside(row, column + i, tiles) +
                    checkUp(row, column + i, tiles) +
                    checkDown(row, column + i, tiles) +
                    checkLeft(row, column + i, tiles) +
                    checkRight(row, column + i, tiles);
        }

        return result;
    }

    private static boolean getOrientation() {
        return RANDOM.nextBoolean();
    }

    private static int getRow(Position orientation, ShipType shipType) {
        int row;

        if (orientation == Position.VERTICAL) {
            row = RANDOM.nextInt(Dimension.FIELD_HEIGHT - shipType.getShipLength() + 1);
        } else {
            row = RANDOM.nextInt(Dimension.FIELD_HEIGHT);
        }

        return row;
    }

    private static int getColumn(Position orientation, ShipType shipType) {
        int column;

        if (orientation == Position.VERTICAL) {
            column = RANDOM.nextInt(Dimension.FIELD_WIDTH);
        } else {
            column = RANDOM.nextInt(Dimension.FIELD_WIDTH - shipType.getShipLength() + 1);
        }

        return column;
    }

    private static int checkInside(int x, int y, Tile[][] tiles) {
        return tiles[x][y].getSipsCount();
    }

    private static int checkUp(int x, int y, Tile[][] tiles) {

        if (x == 0) return 0;

        return tiles[x - 1][y].getSipsCount();
    }

    private static int checkDown(int x, int y, Tile[][] tiles) {

        if (x >= Dimension.FIELD_HEIGHT - 1) return 0;

        return tiles[x + 1][y].getSipsCount();
    }

    private static int checkLeft(int x, int y, Tile[][] tiles) {

        if (y == 0) return 0;

        return tiles[x][y - 1].getSipsCount();
    }

    private static int checkRight(int x, int y, Tile[][] tiles) {

        if (y >= Dimension.FIELD_WIDTH - 1) return 0;

        return tiles[x][y + 1].getSipsCount();
    }
}
