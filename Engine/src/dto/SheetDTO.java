package dto;

import body.impl.Coordinate;
import dto.impl.CellDTO;

public interface SheetDTO {
    String getSheetName();

    int getVersion();

    int getThickness();

    int getWidth();

    int getRowCount();

    int getColumnCount();

    //public CellDTO getCell(String cellID);

    //EffectiveValue getEfectivevalueCell(Coordinate coordinate);

    CellDTO getCell(Coordinate coordinate);
}
