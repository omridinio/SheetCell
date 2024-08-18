package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

public class Divide extends BinaryExpression {

    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        Double res = (Double)evaluate.getValue() / (Double)evaluate2.getValue();
        return new Number(res);
    }

    @Override
    public String getOperationSign() {return "/";}
}
