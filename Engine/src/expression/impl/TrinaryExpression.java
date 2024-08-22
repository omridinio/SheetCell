package expression.impl;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.Serializable;

public abstract class TrinaryExpression implements Expression, Serializable {

    private Expression expression1;
    private Expression expression2;
    private Expression expression3;

    public TrinaryExpression(Expression expression1, Expression expression2, Expression expression3) {
        this.expression1 = expression1;
        this.expression2 = expression2;
        this.expression3 = expression3;
    }

    @Override
    public EffectiveValue evaluate() {
        return evaluate(expression1.evaluate(), expression2.evaluate(), expression3.evaluate());
    }

//not being used
    @Override
    public String toString() {return "(" + expression1 + getOperationSign() + expression2 + ")";}

    abstract protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2, EffectiveValue evaluate3) throws NumberFormatException;

}

