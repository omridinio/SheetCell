package body;

import dto.CellDTO;

public interface Logic {
    CellDTO getCell(String cellId);
    void updateCell(String cellId, String value);
}
