package Properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TargetExpression implements ExpressionUi {
    private StringProperty expression = new SimpleStringProperty("");

    public TargetExpression(String expression) {
        this.expression.set(expression);
    }

    @Override
    public StringProperty getExpression() {
        return new SimpleStringProperty(expression.getValue());
    }



    @Override
    public void setExpression1(ExpressionUi expression1) {

    }

    @Override
    public void setExpression2(ExpressionUi expression2) {

    }

    @Override
    public void setExpression3(ExpressionUi expression3) {

    }

    @Override
    public void setOperator(Operator operator) {

    }

    @Override
    public void setExpression(int index, ExpressionUi expression) {
        this.expression.set(expression.getExpression().getValue());
    }
}
