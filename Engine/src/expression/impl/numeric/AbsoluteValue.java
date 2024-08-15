package expression.impl.numeric;

import expression.api.Expression;
import expression.impl.UnaryExpression;

public class AbsoluteValue  extends UnaryExpression {

    public AbsoluteValue(Expression expression1) {
        super(expression1);
    }

    @Override
    protected Object evaluate(Object evaluate) throws NumberFormatException {
        return Math.abs((Double)evaluate);
    }

    @Override
    public String getOperationSign() {
        return "abs of";
    }
}
