package nbgame.ship;

import java.util.LinkedList;
import java.util.List;

public class Tile {
    private final List<Ship> shipCollection;
    private Hit hit;

    public Tile() {
        this.shipCollection = new LinkedList<>();
        this.hit = Hit.NONE;
    }

    public List<Ship> getShipCollection() {
        return shipCollection;
    }

    public void addShip(Ship ship) {
        this.shipCollection.add(ship);
    }

    public int getSipsCount() {
        return shipCollection.size();
    }

    public Hit getHit() {
        return hit;
    }

    public void setHit(Hit hit) {
        this.hit = hit;
    }
}
