package expression.impl.string;

import expression.api.Expression;
import expression.impl.TrinaryExpression;

public class Sub  extends TrinaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected Object evaluate(Object evaluate, Object evaluate2, Object evaluate3) throws NumberFormatException {
        return ((String)evaluate).substring((int)evaluate2, (int)evaluate3);
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
