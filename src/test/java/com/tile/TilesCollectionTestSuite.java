package com.tile;

import nbgame.constant.FileAccess;
import nbgame.game.BattleField;
import nbgame.ship.Position;
import nbgame.ship.Ship;
import nbgame.ship.ShipType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TilesCollectionTestSuite {

    @Before
    public void before(){
        System.out.println("Test Case: begin");
    }

    @After
    public void after(){
        System.out.println("Test Case: end");
    }

    @Test
    public void testCreateTiles() {

        //Given
        int row = 5;
        int column = 4;
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);

        //When
        int count1 = battleField.getTiles().length;
        int count2 = battleField.getTiles()[1].length;
        System.out.println("Tile[][] length ...");

        //Then
        Assert.assertEquals(10, count1);
        Assert.assertEquals(10, count2);
    }

    @Test
    public void testShipAddOnTiles() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_UP);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);

        //When
        battleField.putShipsOnTiles(ships);
        int count1 = battleField.getTiles()[row][column].getSipsCount();
        int count2 = battleField.getTiles()[row - 1][column].getSipsCount();
        int count3 = battleField.getTiles()[row - 2][column].getSipsCount();
        int count4 = battleField.getTiles()[row - 3][column].getSipsCount();
        int count5 = battleField.getTiles()[row - 4][column].getSipsCount();
        System.out.println("Positioning ship on tiles ....");

        //Then
        Assert.assertEquals(1, count1);
        Assert.assertEquals(1, count2);
        Assert.assertEquals(1, count3);
        Assert.assertEquals(1, count4);
        Assert.assertEquals(0, count5);
    }

    @Test
    public void testTwoShipOverlaidOnTiles() {

        //Given
        int row1 = 5;
        int column1 = 4;
        int row2 = 4;
        int column2 = 3;
        Ship ship1 = new Ship(1, row1, column1, ShipType.FOUR_MAST, Position.VERTICAL_UP);
        Ship ship2 = new Ship(1, row2, column2, ShipType.FOUR_MAST, Position.HORIZONTAL_RIGHT);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);

        //When
        battleField.putShipsOnTiles(ships);
        int count1 = battleField.getTiles()[row1][column1].getSipsCount();
        int count2 = battleField.getTiles()[row1 - 1][column1].getSipsCount();
        int count3 = battleField.getTiles()[row1 - 2][column1].getSipsCount();
        int count4 = battleField.getTiles()[row1 - 3][column1].getSipsCount();
        int count5 = battleField.getTiles()[row1 - 4][column1].getSipsCount();
        System.out.println("Ships overlaid ....");

        //Then
        Assert.assertEquals(1, count1);
        Assert.assertEquals(2, count2);
        Assert.assertEquals(1, count3);
        Assert.assertEquals(1, count4);
        Assert.assertEquals(0, count5);
    }
}
