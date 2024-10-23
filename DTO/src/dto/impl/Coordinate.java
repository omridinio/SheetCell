package dto.impl;

import java.io.Serializable;

public class Coordinate implements Serializable  {
    private final int row;
    private final int column;

    public Coordinate(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Coordinate(Coordinate coordinate) {
        this.row = coordinate.getRow();
        this.column = coordinate.getColumn();
    }

    public Coordinate(String coordinate) {
        this.row = Integer.parseInt(coordinate.substring(1));
        this.column = coordinate.charAt(0) + 1 - 'A';
    }


    public int getRow() {
        return row;
    }


    public int getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return row == that.row && column == that.column;
    }

    @Override
    public int hashCode() {
        return (int) (Math.pow(2,row) * Math.pow(3,column));
    }
    @Override
    public String toString() {
        String res = String.valueOf((char) ('A' + (column - 1)));
        res += Integer.toString(row);
        return res;
    }


}