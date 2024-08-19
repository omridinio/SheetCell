package body;

import dto.SheetDTO;
import dto.impl. CellDTO;

public interface Logic {
    CellDTO getCell(String cellID);
    void updateCell(String cellId, String value);
    SheetDTO getSheet();
    //void printSheet();
}
