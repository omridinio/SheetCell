package dto.impl;

import body.Coordinate;
import body.Sheet;
import dto.SheetDTO;
import body.impl.ImplSheet;
import expression.api.EffectiveValue;
import body.Cell;

public class ImplSheetDTO implements SheetDTO {
    final private Sheet currSheet;


    public ImplSheetDTO(Sheet sheet) {
        this.currSheet = sheet;
    }

    @Override
    public String getSheetName() {
        return currSheet.getSheetName();
    }

    @Override
    public int getVersion() {
        return currSheet.getVersion();
    }

    @Override
    public int getThickness() {
        return currSheet.getThickness();
    }

    @Override
    public int getWidth() {
        return currSheet.getWidth();
    }

    @Override
    public int getRowCount() {
        return currSheet.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return currSheet.getColumnCount();
    }

    @Override
    public EffectiveValue getEfectivevalueCell(Coordinate coordinate) {
        Cell cell = currSheet.getCell(coordinate);
        if (cell == null) {
            return null;
        }
        return cell.getEffectiveValue();
    }




}
