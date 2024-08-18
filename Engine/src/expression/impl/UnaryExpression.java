package expression.impl;
import expression.api.EffectiveValue;
import expression.api.Expression;

public abstract class UnaryExpression implements Expression {

    private Expression expression1;

    public UnaryExpression(Expression expression1) {
        this.expression1 = expression1;
    }

    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression1.evaluate());
    }


    @Override
    public String toString() {return "(" + getOperationSign() + expression1 + ")";}

    abstract protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException;

}

