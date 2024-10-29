package dto.impl;

//import body.Sheet;
//import body.impl.ImplCell;
import dto.SheetDTO;
//import body.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ImplSheetDTO implements SheetDTO {
    final String sheetName;
    final String sheetOwner;
    final int version;
    final int thicknes;
    final int width;
    final int row;
    final int col;
    final Map<Coordinate, CellDTO> activeCells = new HashMap<>();

//    public ImplSheetDTO(Sheet sheet) {
//        sheetName = sheet.getSheetName();
//        version = sheet.getVersion();
//        thicknes = sheet.getThickness();
//        width = sheet.getWidth();
//        row = sheet.getRowCount();
//        col = sheet.getColumnCount();
//        createActiveCells(sheet);
//    }

    public ImplSheetDTO(String sheetName, int version, int thicknes, int width, int row, int col, Map<Coordinate, CellDTO> activeCells, String sheetOwner) {
        this.sheetName = sheetName;
        this.version = version;
        this.thicknes = thicknes;
        this.width = width;
        this.row = row;
        this.col = col;
        this.activeCells.putAll(activeCells);
        this.sheetOwner = sheetOwner;
    }

//    private void createActiveCells(Sheet sheet) {
//        Map<Coordinate, Cell> active = sheet.getActiveCell();
//        for(Coordinate coord : active.keySet()){
//            this.activeCells.put(coord, new CellDTO(active.get(coord)));
//        }
//    }

    @Override
    public String getSheetName() {
        //return currSheet.getSheetName();
        return sheetName;
    }

    @Override
    public String getSheetOwner() {
        return sheetOwner;
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
        CellDTO cell =  activeCells.get(coordinate);
        if(cell == null){
            //cell = new CellDTO(new ImplCell(coordinate.toString()));
            cell = new CellDTO(coordinate.toString(), 1, "", "", "", new ArrayList<Coordinate>(), new ArrayList<Coordinate>());
        }
        return cell;
    }
}
