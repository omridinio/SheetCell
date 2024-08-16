package expression.impl.string;

import expression.api.Expression;
import expression.impl.TrinaryExpression;

public class Sub  extends TrinaryExpression {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected Object evaluate(Object evaluate, Object evaluate2, Object evaluate3) throws NumberFormatException {
        return ((String)((String) evaluate).substring(((Double) evaluate2).intValue(),(Integer)((Double) evaluate3).intValue()));
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
