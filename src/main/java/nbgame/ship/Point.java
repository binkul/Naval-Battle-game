package nbgame.ship;

import java.util.Objects;

public class Point {
    private int row;
    private int column;

    public Point(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Point)) return false;
        Point shipHead = (Point) o;
        return row == shipHead.row &&
                column == shipHead.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }
}
