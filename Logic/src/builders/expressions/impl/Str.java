package builders.expressions.impl;

import builders.expressions.Expression;

public class Str implements Expression {

    private String string;

    public Str(String s) {
        this.string = s;
    }

    @Override
    public Object evaluate() {
        return string;
    }

    @Override
    public String getOperationSign() {
        return "";
    }

    @Override
    public String toString() {
        return string;
    }
}