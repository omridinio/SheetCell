package dto;

import body.Cell;
import body.Coordinate;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;

public interface SheetDTO {
    String getSheetName();

    int getVersion();

    int getThickness();

    int getWidth();

    int getRowCount();

    int getColumnCount();

    //public CellDTO getCell(String cellID);

    EffectiveValue getEfectivevalueCell(Coordinate coordinate);
}
