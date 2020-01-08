package com.logic;

import nbgame.constant.FileAccess;
import nbgame.engine.logic.LogicExpertLevel;
import nbgame.game.BattleField;
import nbgame.ship.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LogicTestSuite {

    @Before
    public void before(){
        System.out.println("Test Case: begin");
    }

    @After
    public void after(){
        System.out.println("Test Case: end");
    }

    @Test
    public void testShootsCollection() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_UP);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);
        LogicExpertLevel logic = new LogicExpertLevel(battleField);

        //When
        logic.getNextShoot();
        logic.getNextShoot();
        logic.getNextShoot();
        int count = logic.getShootsRange().size();
        System.out.println("Remove pints from collection ...");

        //Then
        Assert.assertEquals(97, count);
    }

    @Test
    public void testShootAfterShootTop() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_DOWN);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);
        battleField.putShipsOnTiles(ships);
        LogicExpertLevel logic = new LogicExpertLevel(battleField);

        //When
        logic.addShoot(new Point(row, column));
        Point point = logic.getNextShoot();
        System.out.println("Shoot on top over ship ...");

        //Then
        Assert.assertEquals(row - 1, point.getRow());
        Assert.assertEquals(column, point.getColumn());
    }

    @Test
    public void testShootAfterShootLeft() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_DOWN);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);
        battleField.putShipsOnTiles(ships);
        LogicExpertLevel logic = new LogicExpertLevel(battleField);

        //When
        battleField.getTiles()[row - 1][column].setHit(Hit.HIT);
        logic.addShoot(new Point(row, column));
        Point point = logic.getNextShoot();
        System.out.println("Shoot on left from ship ...");

        //Then
        Assert.assertEquals(row, point.getRow());
        Assert.assertEquals(column - 1, point.getColumn());
    }

    @Test
    public void testDestroyShip() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_DOWN);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);
        battleField.putShipsOnTiles(ships);
        LogicExpertLevel logic = new LogicExpertLevel(battleField);

        //When
        battleField.getTiles()[row - 1][column].setHit(Hit.HIT); //set top
        battleField.getTiles()[row][column - 1].setHit(Hit.HIT); //set left
        logic.addShoot(new Point(row, column));
        Point point1 = logic.getNextShoot();
        Point point2 = logic.getNextShoot();
        Point point3 = logic.getNextShoot();
        System.out.println("Destroy ship ...");

        //Then
        Assert.assertEquals(point1, new Point(row + 1, column));
        Assert.assertEquals(point2, new Point(row + 2, column));
        Assert.assertEquals(point3, new Point(row + 3, column));
    }

    @Test
    public void testRemovePointAroundShip() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.VERTICAL_DOWN);
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        BattleField battleField = new BattleField(null, FileAccess.FIELD_WAVE_PIC_PATH);
        battleField.putShipsOnTiles(ships);
        LogicExpertLevel logic = new LogicExpertLevel(battleField);

        //When
        battleField.getTiles()[row - 1][column].setHit(Hit.HIT); //set top
        battleField.getTiles()[row][column - 1].setHit(Hit.HIT); //set left
        logic.addShoot(new Point(row, column));
        logic.getNextShoot();
        logic.getNextShoot();
        logic.getNextShoot();
        System.out.println("Remove points around ship ...");

        //Then
        Assert.assertEquals(89, logic.getShootsRange().size());
    }
}
