package expression;

import body.Cell;
import expression.api.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class Range implements EffectiveValue {
    private List<Cell> rangeCells = new ArrayList<>();

    public Range(List<Cell> rangeCells) {
        this.rangeCells = rangeCells;
    }



    @Override
    public CellType getCellType() {
        return CellType.RANGE;
    }

    @Override
    public Object getValue() {
        return rangeCells;
    }

    @Override
    public boolean isNaN() {
        return false;
    }

    @Override
    public boolean isUndefined() {
        return false;
    }

    @Override
    public boolean isUnknown() {
        return false;
    }
}
