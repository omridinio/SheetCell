package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;

public class ImplLogic implements Logic {
    private Sheet mainSheet = new ImplSheet("stam",6,6,10,4);

    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.getCell(cellID);
        return new CellDTO(temp);
    }


    public void updateCell(String cellId, String value){
        mainSheet.updateCell(cellId, value);
    }

    @Override
    public SheetDTO getSheet() {
        return new ImplSheetDTO(mainSheet);
    }
}

