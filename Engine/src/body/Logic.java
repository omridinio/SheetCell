package body;

import dto.SheetDTO;
import dto.impl. CellDTO;
import dto.impl.RangeDTO;
import expression.Range;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    RangeDTO getRange(String rangeId);
    List<String> getRangesName();
    List<Integer> getTheRangeOfTheRange(String cellRange);
    void removeRange(String rangeId);
    List<Coordinate> getCoordinateInRange(String cellRange);
    Map<Integer, String> getColumsItem(int col, String theRange);
    Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected);
    void updateDaynmicAnlayze(String cellId, String value);
    Map<Coordinate, CellDTO> getSortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException;
}
