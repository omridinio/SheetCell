package body;

import dto.SheetDTO;
import dto.impl. CellDTO;
import expression.Range;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface Logic {
    CellDTO getCell(String cellID);
    CellDTO getCell(Coordinate coordinate);
    void updateCell(String cellId, String value);
    SheetDTO getSheet();
    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;
    List<Integer> getNumberOfUpdatePerVersion();
    SheetDTO getSheetbyVersion(int version);
    String saveToFile(String name) throws IOException;
    void loadFromFile(String path) throws IOException, ClassNotFoundException;
    List<Sheet> getMainSheet();
    void createNewRange(String rangeId, String range) throws IOException;
    Range getRange(String rangeId);
}
