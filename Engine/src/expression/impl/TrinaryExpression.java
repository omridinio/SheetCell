package expression.impl;
import expression.Expression;

public abstract class TrinaryExpression implements Expression {

    private Expression expression1;
    private Expression expression2;
    private Expression expression3;

    public TrinaryExpression(Expression expression1, Expression expression2, Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public Object evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate(), expression3.evaluate());
    }

    //TODO understand how will this will look
    @Override
    public String toString() {return "(" + expression1 + getOperationSign() + expression2 + ")";}

    abstract protected Object evaluate(Object evaluate, Object evaluate2, Object evaluate3) throws NumberFormatException;

}

