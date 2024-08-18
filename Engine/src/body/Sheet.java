package body;

public interface Sheet {
    String getSheetName();
    Cell getCell(String cellId);
    int getRowCount();
    int getColumnCount();
    int getThickness();
    int getWidth();
    int getVersion();

    void setVersion(int version);
    void updateCell(String cellId, String value);
    void printSheet();

}
