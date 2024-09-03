package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public class Bool implements Expression, EffectiveValue, Serializable {
    private boolean value;
    private boolean isUnknown = false;

    public Bool(boolean value) {
        this.value = value;
    }

    public Bool(boolean value, boolean isUnknown) {
        this.value = value;
        this.isUnknown = isUnknown;
    }

    @Override
    public EffectiveValue evaluate() {
        return new Bool(value);
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String expressionTOtoString() {
        return Boolean.toString(value);
    }

    @Override
    public String toString() {
        if(isUnknown){
            return "UNKNOWN ";
        }
        if(value){
            return "TRUE";
        }
        return "FALSE";
    }

    @Override
    public boolean isNaN() {
        return true;
    }

    @Override
    public boolean isUndefined() {
        return true;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public CellType getCellType() {
        return CellType.BOOLEAN;
    }

    @Override
    public boolean isUnknown() {
        return isUnknown;
    }

    public static boolean checkIsUnknown(EffectiveValue evaluate) {
        return evaluate.getCellType() != CellType.BOOLEAN || evaluate.isUnknown();
    }

    public static boolean checkIsUnknown(EffectiveValue evaluate1, EffectiveValue evaluate2) {
        return evaluate1.getCellType() != CellType.BOOLEAN || evaluate2.getCellType() != CellType.BOOLEAN || evaluate1.isUnknown() || evaluate2.isUnknown();
    }


}
