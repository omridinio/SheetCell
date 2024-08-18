package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

public class Number implements Expression, EffectiveValue {

    private double num;

    public Number(double num) {
        this.num = num;
    }

    public Number(String num) {
        this.num = Double.parseDouble(num);
    }

    @Override
    public EffectiveValue evaluate() {
        return new Number(num);
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return num < 0 ?
                "(" + num + ")" :
                Double.toString(num);
    }


    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    public Object getValue() {
        return num;
    }
}
