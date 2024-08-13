package body;

public interface Sheet {
    String getSheetName();
    Cell getCell(String cellId);
    void updateCell(String cellId, String value);
}
