package body.impl;

import body.Cell;
import body.Sheet;

public class ImplSheet implements Sheet {

    private String sheetName;

    Cell spreadSheet = new ImplCell("A3");

    @Override
    public String getSheetName() {
        return sheetName;
    }
    public Cell getCell(String cellId) {
        return spreadSheet;
    }

    @Override
    public void updateCell(String cellId, String value) {
        spreadSheet.setOriginalValue(value);
    }
}
