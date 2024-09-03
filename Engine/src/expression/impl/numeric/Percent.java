package expression.impl.numeric;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Number;

import java.io.Serializable;

public class Percent extends BinaryExpression implements Serializable {

    public Percent(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "%";
    }

    @Override
    public String expressionTOtoString() {
        return "{PERCENT," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }

    @Override
    public String toString() {
        return "{PERCENT, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate, EffectiveValue evaluate2) throws NumberFormatException {
        if(Number.CheckIsNan(evaluate, evaluate2)){
            return new Number(true);
        }
        Double res = (Double)evaluate.getValue() * (Double)evaluate2.getValue() / 100;
        return new Number(res);
    }



}
