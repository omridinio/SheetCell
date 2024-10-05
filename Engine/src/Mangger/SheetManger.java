package Mangger;

import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import body.impl.ImplSheet;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import jaxb2.generated.STLCell;
import jaxb2.generated.STLRange;
import jaxb2.generated.STLSheet;

import javax.print.attribute.standard.JobMediaSheets;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class SheetManger {
    private Map<String, Logic> sheets = new java.util.HashMap<>();

    public synchronized void addSheet(String sheetName, Logic sheet){
        sheets.put(sheetName, sheet);
    }

    public synchronized void removeSheet(String sheetName){
        sheets.remove(sheetName);
    }

    public synchronized boolean isSheetExist(String sheetName){
        return sheets.containsKey(sheetName);
    }

    public Logic createNewSheet(InputStream file) throws JAXBException {
        Logic logic = new ImplLogic();
        logic.CreateNewSheet(file);
        return logic;
    }


}
