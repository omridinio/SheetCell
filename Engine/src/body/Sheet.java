package body;

import java.util.List;

public interface Sheet {
    String getSheetName();
    Cell getCell(String cellID);
    Cell getCell(Coordinate coordinate);
    int getRowCount();
    int getColumnCount();
    int getThickness();
    int getWidth();
    int getVersion();
    void setVersion(int version);
    void updateCell(String cellId, String value);
    void updateListsOfDependencies(Coordinate coord);
}
