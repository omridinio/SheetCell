package Mangger;

import body.Logic;
import body.impl.ImplLogic;
import dto.impl.SheetBasicData;
import jakarta.xml.bind.JAXBException;

import java.io.InputStream;
import java.util.*;

public class SheetManger {
    private Map<String, Logic> sheets = new HashMap<>();
    private List<Logic> listOfSheets = new ArrayList<>();

    public synchronized void addSheet(String sheetName, Logic sheet){
        sheets.put(sheetName, sheet);
        listOfSheets.add(sheet);
    }

    public synchronized void removeSheet(String sheetName){
        sheets.remove(sheetName);
        listOfSheets.remove(sheetName);
    }

    public synchronized boolean isSheetExist(String sheetName){
        return sheets.containsKey(sheetName);
    }

    public Logic createNewSheet(InputStream file, String owner) throws JAXBException {
        Logic logic = new ImplLogic(owner);
        logic.CreateNewSheet(file);
        return logic;
    }

    public Set<String> getSheetNames(){
        return sheets.keySet();
    }

    public List<SheetBasicData> getSheetBasicData(String userName){
        List<SheetBasicData> res = new ArrayList<>();
        for (Logic logic : sheets.values()) {
            res.add(logic.getSheetBasicData(userName));
        }
        return res;
    }

    public List<SheetBasicData> getSheetBasicData(String userName, int index){
        List<SheetBasicData> res = new ArrayList<>();
        for(int i = index; i < listOfSheets.size() ; i++){
            res.add(listOfSheets.get(i).getSheetBasicData(userName));
        }
        return res;
    }

    public Logic getSheet(String sheetName){
        return sheets.get(sheetName);
    }


}
