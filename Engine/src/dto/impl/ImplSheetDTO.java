package dto.impl;

import body.Coordinate;
import dto.SheetDTO;
import body.impl.ImplSheet;
import expression.api.EffectiveValue;
import body.Cell;

public class ImplSheetDTO implements SheetDTO {
    final private ImplSheet currSheet;

    public ImplSheetDTO() {
        this.currSheet = new ImplSheet();
    }

    public ImplSheetDTO(ImplSheet sheet) {
        this.currSheet = new ImplSheet(sheet);
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
        return currSheet.getCell(coordinate).getEffectiveValue();
    }
}
