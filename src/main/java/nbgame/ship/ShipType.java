package nbgame.ship;

public enum ShipType {
    ONE_MAST(1, 16),
    TWO_MAST(2, 20),
    THREE_MAST(3, 22),
    FOUR_MAST(4, 30);

    private int shipLength;
    private int shipWidthPix;

    ShipType(int shipLength, int shipWidthPix) {
        this.shipLength = shipLength;
        this.shipWidthPix = shipWidthPix;
    }

    public int getShipLength() {
        return shipLength;
    }

    public int getShipWidthPix() {
        return shipWidthPix;
    }
}
