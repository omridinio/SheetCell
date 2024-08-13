package body.impl;

import body.Cell;
import body.Logic;
import body.Sheet;
import dto.CellDTO;

public class ImplLogic implements Logic {
    private Sheet mainSheet = new ImplSheet();

    public CellDTO getCell(String cellId) {
        Cell temp = mainSheet.getCell(cellId);
        return new CellDTO(temp);
    }
    public void updateCell(String cellId, String value){
        mainSheet.updateCell(cellId, value);
    }
}

