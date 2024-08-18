package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

public class Str implements Expression, EffectiveValue {

    private String string;

    public Str(String s) {
        this.string = s;
    }

    @Override
    public EffectiveValue evaluate() {return new Str(string);}

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public CellType getCellType() {
        return null;
    }

    @Override
    public Object getValue() {
        return string;
    }
}