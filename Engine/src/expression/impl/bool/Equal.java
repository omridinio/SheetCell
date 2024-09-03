package expression.impl.bool;

import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class Equal extends BinaryExpression implements Serializable {

        public Equal(Expression expression1, Expression expression2) {
            super(expression1, expression2);
        }

        @Override
        public String getOperationSign() {
            return "==";
        }

        @Override
        public String expressionTOtoString() {
            return "{EQUAL," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
        }

        @Override
        protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2) {
            if(e1.isNaN() || e1.isUndefined() || e1.isUnknown() || e2.isNaN() || e2.isUndefined() || e2.isUnknown()){
                return new Bool(false, true);
            }
            if(e1.getCellType() != e2.getCellType()){
                return new Bool(false);
            }
            return new Bool(e1.getValue().equals(e2.getValue()));
        }

        @Override
        public String toString() {
            return "{EQUAL, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
        }
}
