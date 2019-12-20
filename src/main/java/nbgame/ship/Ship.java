package nbgame.ship;

import java.util.Objects;

public class Ship {
    private int id;
    private ShipType shipType;
    private Point head;
    private VertHoriz orientation;
    private ShipStatus operation;
    private int hit;

    public Ship(int id, int row, int column, ShipType shipType, VertHoriz orientation) {
        this.id = id;
        this.head = new Point(row, column);
        this.shipType = shipType;
        this.orientation = orientation;
        this.operation = ShipStatus.NONE;
        this.hit = 0;
    }

    public int getId() {
        return id;
    }

    public Point getHead() {
        return head;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getLength() {
        return shipType.getShipLength();
    }

    public int getWidthPix() {
        return shipType.getShipWidthPix();
    }

    public VertHoriz getOrientation() {
        return orientation;
    }

    public ShipStatus getOperation() {
        return operation;
    }

    public int getHit() {
        return hit;
    }

    public void setOrientation(VertHoriz orientation) {
        this.orientation = orientation;
    }

    public void setOperation(ShipStatus operation) {
        this.operation = operation;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }

    public Hit getHitResult() {
        if (hit == 0) {
            return Hit.NONE;
        } else if (hit < shipType.getShipLength()) {
            return Hit.HIT;
        } else {
            return Hit.HIT_AND_SINK;
        }

    }

    public boolean isHead(int row, int column) {
        return head.getRow() == row && head.getColumn() == column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Ship)) return false;
        Ship ship = (Ship) o;
        return id == ship.id &&
                shipType == ship.shipType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shipType);
    }
}
