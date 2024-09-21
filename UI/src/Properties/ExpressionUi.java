package Properties;

import javafx.beans.property.StringProperty;

public interface ExpressionUi {
    StringProperty getExpression();
    void setExpression1(ExpressionUi expression1);
    void setExpression2(ExpressionUi expression2);
    void setExpression3(ExpressionUi expression3);
    void setOperator(Operator operator);
    void setExpression(int index, ExpressionUi expression);

}
