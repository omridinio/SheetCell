package expression.impl;

import body.Cell;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public class Reference implements Expression, EffectiveValue,Serializable {

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
        return cell.getEffectiveValue().toString();
    }

    @Override
    public CellType getCellType() {
        return CellType.STRING;
    }
    @Override
    public Object getValue() {
        return cell.getEffectiveValue().getValue();
    }

    public Cell getCell() {
            return cell;
    }

    public void setCell(Cell value) {
        this.cell = value;
    }

    public EffectiveValue getEffectiveValue() {
        return cell.getEffectiveValue();
    }
}