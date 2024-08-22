package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;

public class AbsoluteValue  extends UnaryExpression {

    public AbsoluteValue(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(Number.CheckIsNun(evaluate)){
            return new Number(true);
        }
        Double res = Math.abs((Double)evaluate.getValue());
        return new Number(res);
    }

    @Override
    public String getOperationSign() {
        return "abs of";
    }
}
