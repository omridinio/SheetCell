package expression.impl.string;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Str;
import expression.impl.TrinaryExpression;

import java.io.Serializable;

public class Sub  extends TrinaryExpression implements Serializable {

    public Sub(Expression expression1, Expression expression2, Expression expression3) {
        super(expression1, expression2, expression3);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2, EffectiveValue evaluate3) throws NumberFormatException {
        String res = ((String)((String) evaluate.getValue()).substring(((Double) evaluate2.getValue()).intValue(),(Integer)((Double) evaluate3.getValue()).intValue()));
        return new Str(res);
    }

    @Override
    public String getOperationSign() {
        return "";
    }
}
