package body.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;

import java.util.Map;

public class ImplSheet implements Sheet {

    private int sheetVersion;
    final private String sheetName;
    final private int thickness;
    final private int width;
    final private int row;
    final private int col;
    private Map<Coordinate, Cell> activeCells = new java.util.HashMap<>();


    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
    }

    public ImplSheet(Sheet sheet) {
        this.sheetName = sheet.getSheetName();
        this.thickness = sheet.getThickness();
        this.width = sheet.getWidth();
        this.row = sheet.getRowCount();
        this.col = sheet.getColumnCount();
        this.sheetVersion = sheet.getVersion();
        Map<Coordinate, Cell> copiedActiveCells = new java.util.HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : this.activeCells.entrySet()) {
            Coordinate copiedCoordinate = new CoordinateImpl(entry.getKey());
            Cell copiedCell = new ImplCell(entry.getValue());
            copiedActiveCells.put(copiedCoordinate, copiedCell);
        }
        this.activeCells = copiedActiveCells;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellID) {
        Coordinate coordinate = new CoordinateImpl(cellID);
        if(coordinate.getRow() > row || coordinate.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
        if(!activeCells.containsKey(coordinate)){
            activeCells.put(coordinate, new ImplCell(cellID));
        }
        return activeCells.get(coordinate);
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        if(!activeCells.containsKey(coordinate)){
           return null;
        }
        return activeCells.get(coordinate);
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    @Override
    public int getThickness() {
        return thickness;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public void setVersion(int version) {
        this.sheetVersion = version;
    }

    //TODO check if it work
    @Override
    public void updateCell(String cellId, String value) {
        Coordinate currCoord = new CoordinateImpl(cellId);
        if(currCoord.getRow() > row || currCoord.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
        if(activeCells.containsKey(currCoord)){
            activeCells.get(currCoord).setOriginalValue(value);
        }
        else{
            activeCells.put(currCoord, new ImplCell(cellId));
            activeCells.get(currCoord).setOriginalValue(value);
        }

    }

}
