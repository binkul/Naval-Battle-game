package nbgame.ship;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Tile {
    private final List<Ship> shipCollection;
    private Hit hit;

    public Tile() {
        this.shipCollection = new LinkedList<>();
        this.hit = Hit.NONE;
    }

    public void addShip(Ship ship) {
        this.shipCollection.add(ship);
    }

    public int getSipsCount() {
        return shipCollection.size();
    }
}
