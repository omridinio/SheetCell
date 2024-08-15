package expression.impl;

import expression.Expression;

public class Number implements Expression {

    private double num;

    public Number(double num) {
        this.num = num;
    }

    public Number(String num) {
        this.num = Double.parseDouble(num);
    }

    @Override
    public Object evaluate() {
        return num;
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

}
