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
    private Map<Coordinate, Cell> activeCells;

    //TODO delete it after the rest will work
    Cell spreadSheet = new ImplCell("A3");

    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
    }





    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellId) {
        return null;
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
}
