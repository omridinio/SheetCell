package expression.impl.system;

import expression.Expression;
import expression.impl.UnaryExpression;

//TODO fill reference function
public class Reference  extends UnaryExpression {

    public Reference(Expression expression1) {
        super(expression1);
    }

    @Override
    protected Object evaluate(Object evaluate) throws NumberFormatException {
        return null;
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
