package expression.impl.bool;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class And extends BinaryExpression implements Serializable{

    public And(Expression expression1, Expression expression2) {
        super(expression1, expression2);
    }

    @Override
    public String getOperationSign() {
        return "&&";
    }

    @Override
    public String expressionTOtoString() {
        return "{AND," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2) {
        if(Bool.checkIsUnknown(e1, e2)){
            return new Bool(false, true);
        }
        return new Bool((Boolean)e1.getValue() && (Boolean)e2.getValue());
    }

    @Override
    public String toString() {
        return "{AND, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
    }
}
