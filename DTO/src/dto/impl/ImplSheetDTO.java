package dto.impl;

import dto.SheetDTO;
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
        return version;
    }

    @Override
    public int getThickness() {
        return thicknes;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    @Override
    public CellDTO getCell(Coordinate coordinate) {
        CellDTO cell =  activeCells.get(coordinate);
        if(cell == null){
            cell = new CellDTO(coordinate.toString(), 1, "", "", "", new ArrayList<Coordinate>(), new ArrayList<Coordinate>());
        }
        return cell;
    }
}
