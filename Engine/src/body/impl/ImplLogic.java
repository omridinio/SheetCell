package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb.generated.STLCell;
import jaxb.generated.STLSheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ImplLogic implements Logic  {

    private List<Sheet> mainSheet = new ArrayList<>();


    public ImplLogic() { }

    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(cellID);
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
        InputStream inputStream = new FileInputStream(new File(path));
        STLSheet res = creatGeneratedObject(inputStream);
        mainSheet.add(STLSheet2Sheet(res));
    }

    //TODO: add get version!
    private Sheet STLSheet2Sheet(STLSheet stlSheet) {
        String name = stlSheet.getName();
        int thickness = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int width = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        int row = stlSheet.getSTLLayout().getRows();
        int col = stlSheet.getSTLLayout().getColumns();
        Sheet res = new ImplSheet(name,thickness,width,row,col);
        List<STLCell> listofSTLCells = stlSheet.getSTLCells().getSTLCell();
        for (STLCell stlCell : listofSTLCells) {
            res.setVersion(0);
            String cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            Coordinate coordinate = new CoordinateImpl(cellId);
            res.updateCellDitels(cellId,stlCell.getSTLOriginalValue());
        }
        res.updateCellEffectiveValue("A3");

        return res;
    }

    private STLSheet creatGeneratedObject(InputStream path)throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("jaxb.generated");
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

}

