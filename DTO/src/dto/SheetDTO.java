package dto;

import dto.impl.CellDTO;
import dto.impl.Coordinate;

public interface SheetDTO {
    String getSheetName();

    String getSheetOwner();

    int getVersion();

    int getThickness();

    int getWidth();

    int getRowCount();

    int getColumnCount();

    //public CellDTO getCell(String cellID);

    //EffectiveValue getEfectivevalueCell(Coordinate coordinate);

    CellDTO getCell(Coordinate coordinate);
}
