package dto.impl;

import body.Coordinate;
import body.Sheet;
import dto.SheetDTO;
import body.impl.ImplSheet;
import expression.api.EffectiveValue;
import body.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImplSheetDTO implements SheetDTO {
    //final private Sheet currSheet;
    final String sheetName;
    final int version;
    final int thicknes;
    final int width;
    final int row;
    final int col;
    final Map<Coordinate, CellDTO> activeCells = new HashMap<>();

    public ImplSheetDTO(Sheet sheet) {
        //this.currSheet = sheet;
        sheetName = sheet.getSheetName();
        version = sheet.getVersion();
        thicknes = sheet.getThickness();
        width = sheet.getThickness();
        row = sheet.getRowCount();
        col = sheet.getColumnCount();
        createActiveCells(sheet);
    }

    private void createActiveCells(Sheet sheet) {
        Map<Coordinate, Cell> active = sheet.getActiveCell();
        for(Coordinate coord : active.keySet()){
            this.activeCells.put(coord, new CellDTO(active.get(coord)));
        }
    }

    @Override
    public String getSheetName() {
        //return currSheet.getSheetName();
        return sheetName;
    }

    @Override
    public int getVersion() {
        //return currSheet.getVersion();
        return version;
    }

    @Override
    public int getThickness() {
        //return currSheet.getThickness();
        return thicknes;
    }

    @Override
    public int getWidth() {
        //return currSheet.getWidth();
        return width;
    }

    @Override
    public int getRowCount() {
        //return currSheet.getRowCount();
        return row;
    }

    @Override
    public int getColumnCount() {
        //return currSheet.getColumnCount();
        return col;
    }

//    @Override
//    public EffectiveValue getEfectivevalueCell(Coordinate coordinate) {
//        Cell cell = currSheet.getCell(coordinate);
//        if (cell == null) {
//            return null;
//        }
//        return cell.getEffectiveValue();
//    }

    @Override
    public CellDTO getCell(Coordinate coordinate) {
//        Cell cell = currSheet.getCell(coordinate.toString());
//        return new CellDTO(cell);
        return activeCells.get(coordinate);
    }
}
