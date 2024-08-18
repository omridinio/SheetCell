package body.impl;

import body.Cell;
import body.Logic;
import body.Sheet;
import dto.CellDTO;

public class ImplLogic implements Logic {
    private Sheet mainSheet = new ImplSheet("stam",5,10,3,10);

    public CellDTO getCell(String cellId) {
        Cell temp = mainSheet.getCell(cellId);
        return new CellDTO(temp);
    }

    public void printSheet(){
        mainSheet.printSheet();
    }

    public void updateCell(String cellId, String value){

        mainSheet.updateCell(cellId, value);
    }
}

