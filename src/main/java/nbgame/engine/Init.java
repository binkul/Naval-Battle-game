package nbgame.engine;

import nbgame.constant.Dimension;
import nbgame.game.Settings;
import nbgame.ship.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Init {

    public static List<Ship> createShips(Settings settings) {
        List<Ship> ships = new ArrayList<>();
        int column = 0;
        int id = 1;

        addShip(id, ships, 0, column, settings.getFourMastCount(), ShipType.FOUR_MAST);

        id += settings.getFourMastCount();
        column += settings.getFourMastCount();
        addShip(id, ships, 0, column, settings.getThreeMastCount(), ShipType.THREE_MAST);

        id += settings.getThreeMastCount();
        column += settings.getThreeMastCount();
        addShip(id, ships, 0, column, settings.getTwoMastCount(), ShipType.TWO_MAST);

        id += settings.getTwoMastCount();
        addShip(id, ships, 5, 0, settings.getOneMastCount(), ShipType.ONE_MAST);

        return ships;
    }

    private static void addShip(int id, List<Ship> ships, int row, int column, int count, ShipType shipType) {
        for (int i = 0; i < count; i++) {
            ships.add(new Ship(id, row, column, shipType, Position.VERTICAL));
            column++;
            id++;
        }
    }

    public static Tile[][] createField() {
        Tile[][] tiles = new Tile[Dimension.FIELD_WIDTH][Dimension.FIELD_HEIGHT];
        Arrays.setAll(tiles, x -> {
            Arrays.setAll(tiles[x], y -> new Tile());
            return tiles[x];
        });

        return tiles;
    }

    static void putShipOnField(Ship ship, Tile[][] tiles) {
        int row = ship.getHead().getRow();
        int column = ship.getHead().getColumn();

        for (int i = 0; i < ship.getLength(); i++) {
            tiles[row][column].addShip(ship);
            if (ship.getOrientation() == Position.VERTICAL) {
                row++;
            } else {
                column++;
            }
        }
    }

    public static void clearShipsContent(List<Ship> ships) {
        if (ships == null) return;

        for (Ship ship : ships) {
            ship.setHit(0);
            ship.setOperation(ShipStatus.NONE);
        }
    }

    static List<Ship> deepCopyOfShips(List<Ship> source) {
        List<Ship> ships = new ArrayList<>();
        int id = 1;

        for (Ship sourceShip : source) {
            Ship ship = new Ship(id,
                    sourceShip.getHead().getRow(),
                    sourceShip.getHead().getColumn(),
                    sourceShip.getShipType(),
                    sourceShip.getOrientation());
            ships.add(ship);
            id++;
        }

        return ships;
    }
}
