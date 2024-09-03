package expression.impl.bool;

import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.BinaryExpression;
import expression.impl.Bool;

import java.io.Serializable;

public class Bigger extends BinaryExpression implements Serializable {

        public Bigger(Expression expression1, Expression expression2) {
            super(expression1, expression2);
        }

        @Override
        public String getOperationSign() {
            return ">=";
        }

        @Override
        public String expressionTOtoString() {
            return "{BIGGER," + getExpression1().expressionTOtoString() + "," + getExpression2().expressionTOtoString() + "}";
        }

        @Override
        protected EffectiveValue evaluate(EffectiveValue e1, EffectiveValue e2) {
            if(e1.isNaN() || e2.isNaN()){
                return new Bool(false, true);
            }
            if(e1.getCellType() != CellType.NUMERIC || e2.getCellType() != CellType.NUMERIC){
                return new Bool(false, true);
            }
            return new Bool((Double)e1.getValue() >= (Double)e2.getValue());
        }

        @Override
        public String toString() {
            return "{BIGGER, " + getExpression1().toString() + ", " + getExpression2().toString() + "}";
        }
}
