package nbgame.engine;

import nbgame.ship.Point;
import nbgame.ship.Ship;

public interface Logicable {
    void buildStrategy(Ship ship, Point shootPoint);
    Point getNextShoot();
}
