package dto;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;

public interface SheetDTO {
    public String getSheetName();

    public int getVersion();

    public int getThickness();

    public int getWidth();

    public int getRowCount();

    public int getColumnCount();

    EffectiveValue getEfectivevalueCell(Coordinate coordinate);
}
