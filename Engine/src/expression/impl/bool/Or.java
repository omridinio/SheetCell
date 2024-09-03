package expression.impl.bool;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class Or extends BinaryExpression implements Serializable {

            public Or(Expression expression1, Expression expression2) {
                super(expression1, expression2);
            }

            @Override
            public String getOperationSign() {
                return "||";
            }

            @Override
            public String expressionTOtoString() {
                return "{OR," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
            }

            @Override
            public EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2) {
                if(Bool.checkIsUnknown(e1, e2)){
                    return new Bool(false, true);
                }
                return new Bool((Boolean)e1.getValue() || (Boolean)e2.getValue());
            }

            @Override
            public String toString() {
                return "{OR, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
            }
}
