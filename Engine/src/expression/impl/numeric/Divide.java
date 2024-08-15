package expression.impl.numeric;

import expression.api.Expression;
import expression.impl.BinaryExpression;

public class Divide extends BinaryExpression {

    public Divide(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    protected Object evaluate(Object evaluate, Object evaluate2) throws NumberFormatException {
        return (Double)evaluate / (Double)evaluate2;
    }

    @Override
    public String getOperationSign() {return "/";}
}
