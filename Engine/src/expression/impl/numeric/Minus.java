package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

public class Minus extends BinaryExpression {

    public Minus(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "-";
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2)throws NumberFormatException{
        Double res = (Double)e1.getValue() - (Double)e2.getValue();
        return new Number(res);
    }

}
