package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import expression.impl.Reference;
import jakarta.xml.bind.JAXB;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb.generated.STLCell;
import jaxb.generated.STLSheet;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImplLogic implements Logic {
    //private Sheet mainSheet = new ImplSheet("stam",6,6,10,4);
    private List<Sheet> mainSheet = new ArrayList<Sheet>();

    public ImplLogic() {
        mainSheet.add(new ImplSheet("stam",3,10,5,4));
    }
    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(cellID);
        return new CellDTO(temp);
    }


    public void updateCell(String cellId, String value){
        Sheet newVersion = new ImplSheet((ImplSheet) mainSheet.get(mainSheet.size() - 1));
        newVersion.updateCell(cellId, value);
        mainSheet.add(newVersion);

    }
    @Override
    public void creatNewSheet(String path)throws JAXBException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(new File(path));
        STLSheet res = creatGeneratedObject(inputStream);
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
        //Graph graph = new Graph();
        for (STLCell stlCell : listofSTLCells) {
            String cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            Coordinate coordinate = new CoordinateImpl(cellId);
            res.updateCellDitels(cellId,stlCell.getSTLOriginalValue());

//
//            if(coordinate.getRow() > row || coordinate.getColumn() > col){
//                throw new IllegalArgumentException("Cell is out of bounds");
//            }
//
//            graph.addVertex(coordinate);
        }
        res.updateCellEffectiveValue();

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
}

