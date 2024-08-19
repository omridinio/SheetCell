package body;

import dto.impl. CellDTO;

public interface Logic {
    CellDTO getCell(Coordinate coordinate);
    void updateCell(String cellId, String value);
    void printSheet();
}
