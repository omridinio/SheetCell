package body;

import Mangger.PermissionType;
import body.impl.Coordinate;
import dto.SheetDTO;
import dto.impl. CellDTO;
import dto.impl.RangeDTO;
import dto.impl.SheetBasicData;
import expression.Range;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;


public interface Logic {
    CellDTO getCell(String cellID);
    CellDTO getCell(Coordinate coordinate);
    void updateCell(String cellId, String value);

    void CreateNewSheet(InputStream inputStream) throws JAXBException;

    SheetDTO getSheet();
    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;
    List<Integer> getNumberOfUpdatePerVersion();
    SheetDTO getSheetbyVersion(int version);
    String saveToFile(String name) throws IOException;
    void loadFromFile(String path) throws IOException, ClassNotFoundException;
    List<Sheet> getMainSheet();
    void createNewRange(String rangeId, String range) throws IOException;
    RangeDTO getRange(String rangeId);

    RangeDTO createTempRange(String cellRange);

    List<String> getRangesName();
    List<Integer> getTheRangeOfTheRange(String cellRange);
    void removeRange(String rangeId);
    List<Coordinate> getCoordinateInRange(String cellRange);
    Map<Integer, String> getColumsItem(int col, String theRange);
    Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected);
    void updateDaynmicAnlayze(String cellId, String value);
    Map<Coordinate, CellDTO> getSortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException;
    void deleteSheet();
    String predictCalculate(String expression, String cellID) throws IOException, ClassNotFoundException;

    SheetBasicData getSheetBasicData(String userName);

    String getOwner();

    void addPermission(String username, PermissionType newPermission);
}
