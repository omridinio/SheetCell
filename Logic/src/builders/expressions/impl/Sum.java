package builders.expressions.impl;

import builders.expressions.Expression;

public class Sum extends BinaryExpression {

    public Sum(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "+";
    }

    @Override
    protected Object evaluate(Object e1, Object e2) {
        return (Double)e1 + (Double)e2;
    }

}
