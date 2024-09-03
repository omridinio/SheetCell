package expression.impl.bool;

import expression.impl.UnaryExpression;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Bool;
import java.io.Serializable;

public class Not extends UnaryExpression implements Serializable {

        public Not(Expression expression) {
            super(expression);
        }

        @Override
        public String getOperationSign() {
            return "!";
        }

        @Override
        public String expressionTOtoString() {
            return "{NOT," + getExpression().expressionTOtoString() + "}";
        }

        @Override
        protected EffectiveValue evaluate(EffectiveValue e) {
            if (Bool.checkIsUnknown(e)) {
                return new Bool(false, true);
            }
            return new Bool(!(Boolean) e.getValue());
        }

        @Override
        public String toString() {
            return "{NOT, " + getExpression().toString() + "}";
        }
}
