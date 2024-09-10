package expression;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Range implements Expression, EffectiveValue, Serializable {
    private List<Cell> rangeCells = new ArrayList<>();
    private String rangeId;
    private Coordinate from;
    private Coordinate to;

    public Range(List<Cell> rangeCells, String rangeId) {
        this.rangeCells = rangeCells;
        this.rangeId = rangeId;
    }

    public String getRangeId() {
        return rangeId;
    }

    public List<Cell> getRangeCells() {
        return rangeCells;
    }

    public Coordinate getFrom() {
        return from;
    }

    public Coordinate getTo() {
        return to;
    }

    public void setFrom(Coordinate from) {
        this.from = from;
    }

    public void setTo(Coordinate to) {
        this.to = to;
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

    @Override
    public String toString() {
        return rangeId;
    }

    @Override
    public EffectiveValue evaluate() {
        return new Range(rangeCells, rangeId);
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() {
        return rangeId;
    }
}
