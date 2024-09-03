package expression.impl.bool;

import expression.impl.TrinaryExpression;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Bool;

import java.io.Serializable;

public class If extends TrinaryExpression implements Serializable {

        public If(Expression expression1, Expression expression2, Expression expression3) {
            super(expression1, expression2, expression3);
        }

        @Override
        public String getOperationSign() {
            return "IF";
        }

        @Override
        public String expressionTOtoString() {
            return "{IF," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "," + getExpression3().expressionTOtoString() + "}";
        }

        @Override
        protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2, EffectiveValue e3) {
            if(Bool.checkIsUnknown(e1)){
                return new Bool(false, true);
            }
            if (e2.getCellType() != e3.getCellType()) {
                return new Bool(false, true);
            }
            if ((boolean)e1.getValue()){
                return e2;
            } else {
                return e3;
            }
        }

        @Override
        public String toString() {
            return "{IF, " + getExpression1().toString() + ", " + getExpression2().toString() + ", " + getExpression3().toString() + "}";
        }
}
