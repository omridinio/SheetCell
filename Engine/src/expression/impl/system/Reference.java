package expression.impl.system;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.UnaryExpression;

//TODO fill reference function
public class Reference  extends UnaryExpression {

    public Reference(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        return null;
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
