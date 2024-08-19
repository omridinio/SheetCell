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

    //TODO delete it after the rest will work
    Cell spreadSheet = new ImplCell("A3");

    public ImplSheet() {
        this.sheetName = "Sheet1";
        this.thickness = 4;
        this.width = 10;
        this.row = 5;
        this.col = 4;
        activeCells.put(new CoordinateImpl(1, 1), new ImplCell("A1"));
        activeCells.put(new CoordinateImpl(0, 0), new ImplCell(7));
    }

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
    public Cell getCell(Coordinate coordinate) {
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

    @Override
    public void printSheet() {
        String whiteSpace = makeWidth();
        System.out.print("  "); // Leading space for row numbers
        for (int i = 0; i < col; i++) {
            System.out.print((char) ('A' + i) + whiteSpace + " ");
        }
        System.out.println();

        // Print the rows with numbers and placeholders
        for (int i = 1; i <= row; i++) {
            System.out.print(i); // Print the row number
            if (i < 10) System.out.print("|"); // Extra space for single digit rows
            for (int j = 0; j < col; j++) {
                Coordinate currCoord = new CoordinateImpl(i,j);
                if(activeCells.containsKey(currCoord)){
                    Object currCell = activeCells.get(currCoord).getEffectiveValue().getValue();
                    System.out.print(currCell);
                }
                System.out.print("X"+ whiteSpace + "|"); // Placeholder for cell content
            }
            System.out.println();
        }
    }

    private String makeWidth(){
        String res = " ";
        for (int i = 0; i < width; i++) {
            res += " ";
        }
        return res;
    }
    //TODO check if it work
    @Override
    public void updateCell(String cellId, String value) {
        spreadSheet.setOriginalValue(value);
    }

    public void copy(){


    }
}
