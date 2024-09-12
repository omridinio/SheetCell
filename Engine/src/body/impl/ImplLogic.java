package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import dto.impl.RangeDTO;
import expression.Range;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb2.generated.STLCell;
import jaxb2.generated.STLRange;
import jaxb2.generated.STLSheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ImplLogic implements Logic,Serializable  {

    private List<Sheet> mainSheet = new ArrayList<>();


    public ImplLogic() { }

    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(cellID);
        return new CellDTO(temp);
    }

    @Override
    public Map<Coordinate, CellDTO> getSortRange(String rangeCells, List<Integer> dominantCol) throws IOException, ClassNotFoundException {
        Map<Coordinate,Cell> sortRange = mainSheet.get(mainSheet.size() - 1).sortRange(rangeCells, dominantCol);
        Map<Coordinate, CellDTO> res = new java.util.HashMap<>();
        for (Coordinate coordinate : sortRange.keySet()) {
            res.put(coordinate, new CellDTO(sortRange.get(coordinate)));
        }
        return res;
    }

    @Override
    public List<Integer> getTheRangeOfTheRange(String cellRange){
        return mainSheet.get(mainSheet.size() - 1).getTheRangeOfTheRange(cellRange);
    }

    @Override
    public CellDTO getCell(Coordinate coordinate) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(coordinate.toString());
        return new CellDTO(temp);
    }

    public void updateCell(String cellId, String value){
        Sheet newVersion = null;
        Sheet currentVersion = mainSheet.get(mainSheet.size() - 1);

        try {
            // Step 1: Serialize the object to a byte array
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
            outStream.writeObject(currentVersion);
            outStream.flush();

            // Step 2: Deserialize the byte array into a new object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteInStream);

            newVersion = (Sheet) inStream.readObject();
            newVersion.setUpdateCellCount(0);
            newVersion.updateCell(cellId, value);

            mainSheet.add(newVersion);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void creatNewSheet(String path)throws JAXBException, FileNotFoundException {
        if(!checkPostFix(path)){
            throw new FileNotFoundException("Please enter a '.xml' file only.");
        }
        InputStream inputStream = new FileInputStream(new File(path));
        STLSheet res = creatGeneratedObject(inputStream);
        mainSheet.clear();
        mainSheet.add(STLSheet2Sheet(res));
    }

    private Sheet STLSheet2Sheet(STLSheet stlSheet) {
        String name = stlSheet.getName();
        int thickness = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int width = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        int row = stlSheet.getSTLLayout().getRows();
        int col = stlSheet.getSTLLayout().getColumns();
        Sheet res = new ImplSheet(name,thickness,width,row,col);
        List<STLCell> listofSTLCells = stlSheet.getSTLCells().getSTLCell();
        createRangeList(stlSheet, res);
        for (STLCell stlCell : listofSTLCells) {
            res.setVersion(0);
            String cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            Coordinate coordinate = new CoordinateImpl(cellId);
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
        return new ImplSheetDTO(mainSheet.get(mainSheet.size() - 1));
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
        return new ImplSheetDTO(mainSheet.get(version));
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
        return new RangeDTO(mainSheet.get(mainSheet.size() - 1).getRange(rangeId));
    }

    @Override
    public List<String> getRangesName() {
        return mainSheet.get(mainSheet.size() - 1).getRangeName();
    }

    @Override
    public void removeRange(String rangeId) {
        mainSheet.get(mainSheet.size() - 1).removeRange(rangeId);
    }

    @Override
    public List<Coordinate> getCoordinateInRange(String cellRange) {
        return mainSheet.get(mainSheet.size() - 1).getCoordinateInRange(cellRange);
    }

}

