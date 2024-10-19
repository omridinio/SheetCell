package body;

import dto.impl.PermissionType;
import body.impl.Coordinate;
import dto.SheetDTO;
import dto.impl. CellDTO;
import dto.impl.RangeDTO;
import dto.impl.SheetBasicData;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;


public interface Logic {
    CellDTO getCell(String cellID);

    List<Integer> getTheRangeOfTheRange(String cellRange, int version);

    CellDTO getCell(Coordinate coordinate);
    void updateCell(String cellId, String value, String userNameUpdate);

    void CreateNewSheet(InputStream inputStream, String userName) throws JAXBException;


    SheetDTO getSheet();
    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;
    List<Integer> getNumberOfUpdatePerVersion();
    SheetDTO getSheetbyVersion(int version);
    String saveToFile(String name) throws IOException;
    void loadFromFile(String path) throws IOException, ClassNotFoundException;
    List<Sheet> getMainSheet();
    void createNewRange(String rangeId, String range) throws IOException;
    RangeDTO getRange(String rangeId);

    RangeDTO getRange(String rangeId, int version);

    RangeDTO createTempRange(String cellRange);

    RangeDTO createTempRange(String cellRange, int version);

    List<String> getRangesName();

    Map<Coordinate, CellDTO> getSortRange(int version, String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException;

    List<Integer> getTheRangeOfTheRange(String cellRange);

    List<String> getRangesName(int version);

    void removeRange(String rangeId);
    List<Coordinate> getCoordinateInRange(String cellRange);

    List<Coordinate> getCoordinateInRange(int version, String cellRange);

    Map<Integer, String> getColumsItem(int col, String theRange);

    Map<Integer, String> getColumsItem(int col, String theRange, int version);

    Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected);
    void updateDaynmicAnlayze(String cellId, String value);

    String getSheetName();

    Map<Coordinate, CellDTO> getSortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException;

    Sheet copySheet() throws IOException, ClassNotFoundException;

    Sheet copySheetByVersion(int version) throws IOException, ClassNotFoundException;

    void deleteSheet();

    Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected, int version);

    String predictCalculate(String expression, String cellID) throws IOException, ClassNotFoundException;

    String predictCalculate(String expression, String cellID, int version) throws IOException, ClassNotFoundException;

    SheetBasicData getSheetBasicData(String userName);

    String getOwner();

    void addPermission(String username, PermissionType newPermission);

    PermissionType getPermission(String userName);

    int getVersion();

}
