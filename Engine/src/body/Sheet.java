package body;

import dto.impl.CellDTO;
import expression.Range;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface Sheet {
    String getSheetName();
    Cell getCell(String cellID);
    Cell getCell(Coordinate coordinate);
    int getRowCount();
    int getColumnCount();
    int getThickness();
    int getWidth();
    int getVersion();
    int getCountUpdateCell();
    void addNewRange(String rangeId, String cellRange);
    void setVersion(int version);
    void setUpdateCellCount(int countUpdateCell);
    void updateCell(String cellId, String value);
    void updateListsOfDependencies(Coordinate coord);
    void updateCellDitels(String cellId, String value);
    void updateCellEffectiveValue(String cellId);
    Range getRange(String rangeId);
    List<String> getRangeName();
    List<Integer> getTheRangeOfTheRange(String cellRange);
    void removeRange(String rangeId);
    Map<Coordinate, Cell> sortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException;
}
