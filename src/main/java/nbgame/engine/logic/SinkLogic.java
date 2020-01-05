package nbgame.engine.logic;

import nbgame.ship.Point;

public interface SinkLogic extends ShootLogic{
    Point destroyShip();
    Point hitShipHorizontal();
    Point hitShipVertical();
}
