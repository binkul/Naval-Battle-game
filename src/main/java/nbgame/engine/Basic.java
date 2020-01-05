package nbgame.engine;

import nbgame.constant.Dimension;
import nbgame.game.Settings;
import nbgame.ship.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Basic {

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
            if (ship.getOrientation() == Position.VERTICAL_DOWN) {
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
