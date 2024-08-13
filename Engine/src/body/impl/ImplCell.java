package body.impl;

import body.Cell;
import method.expression.Expression;
import method.expression.impl.*;
import method.expression.impl.Number;

import java.util.ArrayList;
import java.util.List;

public class ImplCell implements Cell {

    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private Expression effectiveValue;
    private List<Cell> cellsDependsOnThem = new ArrayList<>();
    private List<Cell> cellsDependsOnHim =new ArrayList<>();

    public ImplCell(String id) {
        Id = id;
        lastVersionUpdate = 1;
        originalValue = "";
    }

    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void setId(String id) {
        Id = id;
    }

    @Override
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }

    @Override
    public void setLastVersionUpdate(int version) {
        lastVersionUpdate = version;
    }

    @Override
    public String getOriginalValue() {
        return originalValue;
    }

    @Override
    public void setOriginalValue(String original) {
        originalValue = original;
        effectiveValue = stringToExpression(originalValue);
    }

    @Override
    public Object getEffectiveValue()throws NumberFormatException {return effectiveValue.evaluate();
    }

    @Override
    public List<Cell> getCellsDependsOnThem() {
        return cellsDependsOnThem;
    }

    @Override
    public Expression getExpression() {
        return effectiveValue;
    }
    private Expression stringToExpression(String input) {
        if(input.isEmpty()){
            return null;
        }
        input = input.substring(1, input.length() - 1);
        List<Expression> e = new ArrayList<>();
        if(!input.contains("{")) {
            String[] expression = input.split(",");

            for (int i = 1; i < expression.length; i++){
                try{
                    Double.parseDouble(expression[i]);
                    e.add(new Number(expression[i]));
                }catch (NumberFormatException error){
                    e.add(new Str(expression[i]));
                }
            }
            return createExpression(expression[0],e);
        }
        else{
            int open=0;
            int close=0;
            String expression = input.split(",")[0];
            for (int i = 0; i < input.length(); i++) {
                char ch = input.charAt(i);
                if (ch == '{') {
                    open = i;
                }
                if (ch == '}') {
                    close = i+1;
                    Expression exp = stringToExpression(input.substring(open, close));
                    e.add(exp);
                }
            }
            return createExpression(expression,e);
        }

    }


    private Expression createExpression(String operator, List<Expression> args) {
        return switch (operator) {
            case "PLUS" -> new Plus(args.get(0), args.get(1));
            case "MINUS" -> new Minus(args.get(0), args.get(1));
            case "POW" -> new Pow(args.get(0), args.get(1));
            case "CONCAT" -> new Concat(args.get(0), args.get(1));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
