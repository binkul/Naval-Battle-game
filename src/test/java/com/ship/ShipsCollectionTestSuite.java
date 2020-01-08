package com.ship;

import nbgame.ship.Hit;
import nbgame.ship.Position;
import nbgame.ship.Ship;
import nbgame.ship.ShipType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ShipsCollectionTestSuite {

    @Before
    public void before(){
        System.out.println("Test Case: begin");
    }

    @After
    public void after(){
        System.out.println("Test Case: end");
    }

    @Test
    public void testShipAddToList() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.HORIZONTAL_RIGHT);

        //When
        List<Ship> ships = new ArrayList<>();
        ships.add(ship);
        int size = ships.size();
        int length = ships.get(0).getLength();
        int hits = ships.get(0).getHit();
        Hit hitResult = ships.get(0).getHitResult();
        int pixWidth = ships.get(0).getWidthPix();
        System.out.println("Testing the ship addition ... ");

        //Then
        Assert.assertEquals(1, size);
        Assert.assertEquals(4, length);
        Assert.assertEquals(0, hits);
        Assert.assertEquals(30, pixWidth);
        Assert.assertEquals(row, ships.get(0).getHead().getRow());
        Assert.assertEquals(column, ships.get(0).getHead().getColumn());
        Assert.assertEquals(Hit.NONE, hitResult);

    }

    @Test
    public void testShipHitNotSink() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.HORIZONTAL_RIGHT);

        //When
        ship.setHit(ship.getHit() + 1);
        Hit hitResult = ship.getHitResult();
        System.out.println("Testing one hit ...");

        //Then
        Assert.assertEquals(Hit.HIT, hitResult);
    }

    @Test
    public void testShipHitAndSink() {

        //Given
        int row = 5;
        int column = 4;
        Ship ship = new Ship(1, row, column, ShipType.FOUR_MAST, Position.HORIZONTAL_RIGHT);

        //When
        ship.setHit(ship.getHit() + 1);
        ship.setHit(ship.getHit() + 1);
        ship.setHit(ship.getHit() + 1);
        ship.setHit(ship.getHit() + 1);
        Hit hitResult = ship.getHitResult();
        System.out.println("Testing hit and sink ...");

        //Then
        Assert.assertEquals(Hit.HIT_AND_SINK, hitResult);
    }

}
