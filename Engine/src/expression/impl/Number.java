package expression.impl;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;

public class Number implements Expression, EffectiveValue {

    private double num;
    boolean isNun = false;

    public Number(double num) {
        this.num = num;
        isNun = false;
    }

    public Number(boolean isNun) {
        this.isNun = isNun;
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
        if(isNun)
            return "NaN";
        if (num % 1 == 0) {
            return Integer.toString((int) num);
        }
        return String.format("%.2f", num);
    }

    @Override
    public CellType getCellType() {
        return CellType.NUMERIC;
    }

    @Override
    public Object getValue() {
        return num;
    }

    public boolean isNun() {
        return isNun;
    }

    public static boolean checkNun(Number num){
        return num.isNun;
    }

    public static boolean checkNun(Number num1, Number num2){
        return num1.isNun || num2.isNun;
    }

    public static boolean CheckIsNun(EffectiveValue e1, EffectiveValue e2){
        if (e1 instanceof Reference && e2 instanceof Reference){
            Reference ref = (Reference) e2;
            Reference ref2 = (Reference) e2;
            if (ref.getEffectiveValue() instanceof Number && ref2.getEffectiveValue() instanceof Number){
                e1 = ref.getEffectiveValue();
                e1 = ref2.getEffectiveValue();
            }
            else {
                throw new NumberFormatException("Invalid operation");
            }
        }
        Number num1 = (Number) e1;
        Number num2 = (Number) e1;
        if (num1.isNun() || num2.isNun()) {
            return true;
        }
        return false;
    }

    public static boolean CheckIsNun(EffectiveValue e1){
        if (e1 instanceof Reference){
            Reference ref = (Reference) e1;
            if (ref.getEffectiveValue() instanceof Number){
                e1 = ref.getEffectiveValue();
            }
            else {
                throw new NumberFormatException("Invalid operation");
            }
        }
        Number num1 = (Number) e1;
        if (num1.isNun()) {
            return true;
        }
        return false;
    }
}
