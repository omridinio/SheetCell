package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.impl.CellDTO;

public class ImplLogic implements Logic {
    private Sheet mainSheet = new ImplSheet("stam",5,10,3,10);

    public CellDTO getCell(Coordinate coordinate) {
        Cell temp = mainSheet.getCell(coordinate);
        return new CellDTO(temp);
    }

    public void printSheet(){
        mainSheet.printSheet();
    }

    public void updateCell(String cellId, String value){

        mainSheet.updateCell(cellId, value);
    }
}

