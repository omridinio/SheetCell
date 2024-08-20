package expression.impl;

import body.Cell;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

public class Reference implements Expression, EffectiveValue {

    private Cell cell;

    public Reference(Cell cell) {
        this.cell = cell;
    }

    @Override
    public EffectiveValue evaluate() {return new Reference(cell);}

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }

    @Override
    public Object getValue() {
        return cell.getEffectiveValue();
    }
}