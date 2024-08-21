package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;

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
        newVersion.setVersion(newVersion.getVersion() + 1);
        mainSheet.add(newVersion);

    }

    @Override
    public SheetDTO getSheet() {
        return new ImplSheetDTO(mainSheet.get(mainSheet.size() - 1));
    }
}

