package Properties;


import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class ExpressionUiImpl implements ExpressionUi {

    private List<ExpressionUi> expressions = new ArrayList<>(3);
    private Operator operator;
    private StringProperty expressionStr = new SimpleStringProperty("");

    public ExpressionUiImpl() {
        expressions.add(new TargetExpression(""));
        expressions.add(new TargetExpression(""));
        expressions.add(new TargetExpression(""));
        initExpressionStr();
    }

    public ExpressionUiImpl(ExpressionUi expression1, ExpressionUi expression2, ExpressionUi expression3, Operator operator) {
        expressions.add(expression1);
        expressions.add(expression2);
        expressions.add(expression3);
        this.operator = operator;
        initExpressionStr();
    }

    public ExpressionUiImpl(ExpressionUi expression1, ExpressionUi expression2, Operator operator) {
        expressions.add(expression1);
        expressions.add(expression2);
        expressions.add(new TargetExpression(""));
        this.operator = operator;
        initExpressionStr();
    }

    public ExpressionUiImpl(ExpressionUi expression1, Operator operator) {
        expressions.add(expression1);
        expressions.add(new TargetExpression(""));
        expressions.add(new TargetExpression(""));
        this.operator = operator;
        initExpressionStr();
    }

    public ExpressionUiImpl(Operator operator) {
        this.operator = operator;
        expressions.add(new TargetExpression(""));
        expressions.add(new TargetExpression(""));
        expressions.add(new TargetExpression(""));
        initExpressionStr();
    }

    public void initExpressionStr() {
        switch (operator) {
            case CUSTOM -> expressionStr.set("");
            case ABS -> expressionStr.bind(Bindings.concat("{ABS,", expressions.get(0).getExpression(), "}"));
            case PLUS ->
                    expressionStr.bind(Bindings.concat("{PLUS,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case MINUS ->
                    expressionStr.bind(Bindings.concat("{MINUS,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case TIMES -> expressionStr.bind(Bindings.concat("{TIMES,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case DIVIDE -> expressionStr.bind(Bindings.concat("{DIVIDE,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case MOD -> expressionStr.bind(Bindings.concat("{MOD,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case POW -> expressionStr.bind(Bindings.concat("{POW,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case SUM -> expressionStr.bind(Bindings.concat("{SUM,", expressions.get(0).getExpression(), "}"));
            case AVERAGE -> expressionStr.bind(Bindings.concat("{AVERAGE,", expressions.get(0).getExpression(), "}"));
            case PERCENT -> expressionStr.bind(Bindings.concat("{PERCENT,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case EQUAL -> expressionStr.bind(Bindings.concat("{EQUAL,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case NOT -> expressionStr.bind(Bindings.concat("{NOT,", expressions.get(0).getExpression(), "}"));
            case OR -> expressionStr.bind(Bindings.concat("{OR,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case AND -> expressionStr.bind(Bindings.concat("{AND,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case BIGGER -> expressionStr.bind(Bindings.concat("{BIGGER,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case LESS -> expressionStr.bind(Bindings.concat("{LESS,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case IF -> expressionStr.bind(Bindings.concat("{IF,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), ",", expressions.get(2).getExpression(), "}"));
            case REF -> expressionStr.bind(Bindings.concat("{REF,", expressions.get(0).getExpression(), "}"));
            case CONCAT -> expressionStr.bind(Bindings.concat("{CONCAT,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), "}"));
            case SUB -> expressionStr.bind(Bindings.concat("{SUB,", expressions.get(0).getExpression(), ",", expressions.get(1).getExpression(), ",", expressions.get(2).getExpression(), "}"));
        }
    }

    @Override
    public StringProperty getExpression() {
        return expressionStr;
    }

    @Override
    public void setExpression1(ExpressionUi expression1) {
        expressions.set(0, expression1);
    }

    @Override
    public void setExpression2(ExpressionUi expression2) {
        expressions.set(1, expression2);
    }

    @Override
    public void setExpression3(ExpressionUi expression3) {
        expressions.set(2, expression3);
    }

    @Override
    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    @Override
    public void setExpression(int index, ExpressionUi expression) {
        expressions.set(index, expression);
        initExpressionStr();
    }


}
