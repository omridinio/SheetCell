package expression.impl.numeric;

import body.Cell;
import expression.CellType;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.UnaryExpression;


import java.io.Serializable;
import java.util.List;

public class Average extends UnaryExpression implements Serializable {

    public Average(Expression expression1) {
        super(expression1);
    }

    @Override
    protected EffectiveValue evaluate(EffectiveValue evaluate) throws NumberFormatException {
        if(evaluate.getCellType() != CellType.RANGE){
            return new Number(true);
        }
        List<Cell> cells = (List<Cell>) evaluate.getValue();
        double sum = 0;
        int len = 0;
        for (Cell cell : cells) {
            if(cell.getEffectiveValue().isNaN()){
                return new Number(true);
            }
            if (cell.getEffectiveValue().getCellType() != CellType.NUMERIC) {
                continue;
            }
            sum += (Double) (cell.getEffectiveValue().getValue());
            len++;
        }
        if(len == 0)
            throw new NumberFormatException("All the cells are not numeric");
        return new Number(sum/len);
    }

    @Override
    public String getOperationSign() {
        return "AVERAGE of";
    }

    @Override
    public String expressionTOtoString() {
        return "{AVERAGE," + getExpression().expressionTOtoString() + "}";
    }

    @Override
    public String toString() {
        return "{AVERAGE, " + getExpression().toString() + "}";
    }
}
