package body.impl;

import DTOCreator.DTOCreator;
import dto.impl.Coordinate;
import dto.impl.PermissionType;
import body.Cell;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.RangeDTO;
import dto.impl.SheetBasicData;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb2.generated.STLCell;
import jaxb2.generated.STLRange;
import jaxb2.generated.STLSheet;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplLogic implements Logic,Serializable  {

    private List<Sheet> mainSheet = new ArrayList<>();
    private String sheetName;
    private final String owner;
    private Map<String, PermissionType> premmison = new HashMap<>();
    private String size;

    public ImplLogic() {
        this.owner = "";
    }

    public ImplLogic(String owner) {
        this.owner = owner;
    }

    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(cellID);
        //return new CellDTO(temp);
        return DTOCreator.createCellDTO(temp);
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Map<Coordinate, CellDTO> getSortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException {
        Map<Coordinate,Cell> sortRange = mainSheet.get(mainSheet.size() - 1).sortRange(rangeCells, dominantCol);
        Map<Coordinate, CellDTO> res = new java.util.HashMap<>();
        for (Coordinate coordinate : sortRange.keySet()) {
            //res.put(coordinate, new CellDTO(sortRange.get(coordinate)));
            res.put(coordinate, DTOCreator.createCellDTO(sortRange.get(coordinate)));
        }
        return res;
    }

    @Override
    public Map<Coordinate, CellDTO> getSortRange(int version, String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException {
        Map<Coordinate,Cell> sortRange = mainSheet.get(version - 1).sortRange(rangeCells, dominantCol);
        Map<Coordinate, CellDTO> res = new java.util.HashMap<>();
        for (Coordinate coordinate : sortRange.keySet()) {
            //res.put(coordinate, new CellDTO(sortRange.get(coordinate)));
            res.put(coordinate, DTOCreator.createCellDTO(sortRange.get(coordinate)));
        }
        return res;
    }

    @Override
    public List<Integer> getTheRangeOfTheRange(String cellRange){
        return mainSheet.get(mainSheet.size() - 1).getTheRangeOfTheRange(cellRange);
    }

    @Override
    public List<Integer> getTheRangeOfTheRange(String cellRange, int version){
        return mainSheet.get(version - 1).getTheRangeOfTheRange(cellRange);
    }

    @Override
    public CellDTO getCell(Coordinate coordinate) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(coordinate.toString());
        //return new CellDTO(temp);
        return DTOCreator.createCellDTO(temp);
    }

    @Override
    public void updateCell(String cellId, String value, String userNameUpdate){
        try{
            Sheet newVersion = copySheet();
            newVersion.setLastUserUpdate(userNameUpdate);
            newVersion.setUpdateCellCount(0);
            newVersion.updateCell(cellId, value);
            mainSheet.add(newVersion);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void updateDaynmicAnlayze(String cellId, String value) {
        try {
            Sheet newVersion = copySheet();
            newVersion.dynmicAnlayzeUpdate(cellId, value);
            mainSheet.add(newVersion);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Sheet copySheet() throws IOException, ClassNotFoundException {
        return copySheetByVersion(mainSheet.size());
    }

    @Override
    public Sheet copySheetByVersion(int version) throws IOException, ClassNotFoundException {
        Sheet newVersion = null;
        Sheet currentVersion = mainSheet.get(version - 1);
        // Step 1: Serialize the object to a byte array
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
        outStream.writeObject(currentVersion);
        outStream.flush();

        // Step 2: Deserialize the byte array into a new object
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        ObjectInputStream inStream = new ObjectInputStream(byteInStream);
        newVersion = (Sheet) inStream.readObject();
        return newVersion;
    }


    @Override
    public void deleteSheet() {
        mainSheet.remove(mainSheet.size() - 1);
    }

    @Override
    public void creatNewSheet(String path)throws JAXBException, FileNotFoundException {

    }

    @Override
    public void CreateNewSheet(InputStream inputStream, String userName) throws JAXBException {
        STLSheet res = creatGeneratedObject(inputStream);
        Sheet newSheet = STLSheet2Sheet(res, userName);
        mainSheet.clear();
        mainSheet.add(newSheet);
        sheetName = newSheet.getSheetName();
        size = mainSheet.get(0).getSize();
    }


    private Sheet STLSheet2Sheet(STLSheet stlSheet, String userName) {
        String name = stlSheet.getName();
        int thickness = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int width = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        int row = stlSheet.getSTLLayout().getRows();
        int col = stlSheet.getSTLLayout().getColumns();
        Sheet res = new ImplSheet(name,thickness,width,row,col, userName, owner);
        List<STLCell> listofSTLCells = stlSheet.getSTLCells().getSTLCell();
        createRangeList(stlSheet, res);
        for (STLCell stlCell : listofSTLCells) {
            res.setVersion(0);
            String cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            Coordinate coordinate = new Coordinate(cellId);
            res.updateCellDitels(cellId,stlCell.getSTLOriginalValue());
        }
        res.updateCellEffectiveValue("A3");

        return res;
    }

    private void createRangeList(STLSheet stlSheet, Sheet currShit) {
        List<STLRange> listofSTLRanges = stlSheet.getSTLRanges().getSTLRange();
        for (STLRange stlRange : listofSTLRanges) {
            String rangeId = stlRange.getName();
            String range = stlRange.getSTLBoundaries().getFrom() + ".." + stlRange.getSTLBoundaries().getTo();
            currShit.addNewRange(rangeId, range);
        }
    }

    private STLSheet creatGeneratedObject(InputStream path)throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("jaxb2.generated");
        Unmarshaller u = jc.createUnmarshaller();
        return (STLSheet) u.unmarshal(path);
    }
    @Override
    public SheetDTO getSheet() {
        return DTOCreator.createSheetDTO(mainSheet.get(mainSheet.size() - 1));
    }

    public List<Integer> getNumberOfUpdatePerVersion(){
        List<Integer> res = new ArrayList<Integer>();
        for (Sheet sheet : mainSheet) {
            res.add(sheet.getCountUpdateCell());
        }
        return res;
    }

    @Override
    public SheetDTO getSheetbyVersion(int version) {
        return DTOCreator.createSheetDTO(mainSheet.get(version));
    }

    private boolean checkPostFix(String fullPath) {
        return fullPath.endsWith(".xml");
    }

    @Override
    public String saveToFile(String name)throws IOException{
        List<Sheet> currentVersion = this.mainSheet;
        // Step 1: Serialize the object to a file
        File file = new File(name);
        FileOutputStream fileOutStream = new FileOutputStream(file);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOutStream);
        outStream.writeObject(currentVersion);
        outStream.flush();
        outStream.close();
        fileOutStream.close();
        return file.getAbsolutePath();
    }

    public List<Sheet> getMainSheet() {
        return mainSheet;
    }

    @Override
    public void createNewRange(String rangeId, String range) throws IOException {
        mainSheet.get(mainSheet.size() - 1).addNewRange(rangeId, range);
    }

    @Override
    public void loadFromFile(String path) throws IOException,  ClassNotFoundException{
        FileInputStream fileInStream = new FileInputStream(path);
        ObjectInputStream inStream = new ObjectInputStream(fileInStream);
        mainSheet = (List<Sheet>) inStream.readObject();
        inStream.close();
        fileInStream.close();
    }

    @Override
    public RangeDTO getRange(String rangeId) {
        return DTOCreator.createRangeDTO(mainSheet.get(mainSheet.size() - 1).getRange(rangeId.toUpperCase()));
    }

    @Override
    public RangeDTO getRange(String rangeId, int version) {
        return DTOCreator.createRangeDTO(mainSheet.get(version - 1).getRange(rangeId.toUpperCase()));
    }

    @Override
    public RangeDTO createTempRange(String cellRange) {
        return DTOCreator.createRangeDTO(mainSheet.get(mainSheet.size() - 1).createTempRange(cellRange));
    }

    @Override
    public RangeDTO createTempRange(String cellRange, int version) {
        return DTOCreator.createRangeDTO(mainSheet.get(version - 1).createTempRange(cellRange));
    }

    @Override
    public List<String> getRangesName() {
        return mainSheet.get(mainSheet.size() - 1).getRangeName();
    }

    @Override
    public List<String> getRangesName(int version) {
        return mainSheet.get(version - 1).getRangeName();
    }

    @Override
    public void removeRange(String rangeId) {
        mainSheet.get(mainSheet.size() - 1).removeRange(rangeId);
    }

    @Override
    public List<Coordinate> getCoordinateInRange(String cellRange) {
        return mainSheet.get(mainSheet.size() - 1).getCoordinateInRange(cellRange);
    }

    @Override
    public List<Coordinate> getCoordinateInRange(int version, String cellRange) {
        return mainSheet.get(version - 1).getCoordinateInRange(cellRange);
    }

    @Override
    public Map<Integer, String> getColumsItem(int col, String theRange){
        return mainSheet.get(mainSheet.size() - 1).getColumsItem(col, theRange);
    }

    @Override
    public Map<Integer, String> getColumsItem(int col, String theRange, int version){
        return mainSheet.get(version - 1).getColumsItem(col, theRange);
    }

    @Override
    public Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected){
        return mainSheet.get(mainSheet.size() - 1).getColumsItem(col, theRange, rowSelected);
    }

    @Override
    public Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected, int version){
        return mainSheet.get(version - 1).getColumsItem(col, theRange, rowSelected);
    }

    @Override
    public String predictCalculate(String expression, String cellID) throws IOException, ClassNotFoundException {
        Sheet newVersion = copySheet();
        return newVersion.predictCalculate(expression, cellID);
    }

    @Override
    public String predictCalculate(String expression, String cellID, int version) throws IOException, ClassNotFoundException {
        Sheet newVersion = copySheetByVersion(version);
        return newVersion.predictCalculate(expression, cellID);
    }

    @Override
    public SheetBasicData getSheetBasicData(String userName) {
        return new SheetBasicData(sheetName, owner, premmison ,size, userName);
    }

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void addPermission(String username, PermissionType newPermission){
        premmison.put(username, newPermission);
    }

    @Override
    public PermissionType getPermission(String userName){
        return premmison.get(userName);
    }

    @Override
    public int getVersion() {
        return mainSheet.size();
    }

}

