package expression.impl.numeric;

import body.Cell;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;


import java.io.Serializable;
import java.util.List;

public class Sum extends UnaryExpression implements Serializable {

        public Sum(Expression expression1) {
            super(expression1);
        }

        @Override
        protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
            if(evaluate.getCellType() != CellType.RANGE){
                return new Number(true);
            }
            List<Cell> cells = (List<Cell>) evaluate.getValue();
            double sum = 0;
            for (Cell cell : cells) {
                if(cell.getEffectiveValue().isNaN() || (cell.getEffectiveValue().getCellType() != CellType.NUMERIC || cell.getEffectiveValue().getCellType() != CellType.EMPTY)){
                    return new Number(true);
                }
                if (cell.getEffectiveValue().getCellType() == CellType.EMPTY) {
                    continue;
                }
                sum += (Double) cell.getEffectiveValue().getValue();
            }
            return new Number(sum);
        }

        @Override
        public String getOperationSign() {
            return "sum of";
        }

        @Override
        public String expressionTOtoString() {
            return "{SUM," + getExpression().expressionTOtoString() + "}";
        }

        @Override
        public String toString() {
            return "{SUM, " + getExpression().toString() + "}";
        }
}
