package body.impl;

import body.Cell;
import expression.api.Expression;
import expression.impl.*;
import expression.impl.Number;
import expression.impl.numeric.*;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.Reference;

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

        if(!input.contains(",")){
            try{
                Double.parseDouble(input);
                return (new Number(input));
            }catch (NumberFormatException error){
                return (new Str(input));
            }
        }

        else {
            input = input.substring(1, input.length() - 1);
            List<String> result = new ArrayList<>();
            List<Expression> e = new ArrayList<>();
            StringBuilder currentElement = new StringBuilder();
            boolean insideBraces = false;
            int openBracket = 0;
            for (char c : input.toCharArray()) {
                if (c == '{') {
                    insideBraces = true;
                    openBracket++;
                } else if (c == '}') {
                    openBracket--;
                    if(openBracket == 0){
                        insideBraces = false;
                    }
                }
                if (c == ',' && !insideBraces) {
                    result.add(currentElement.toString().trim());

                    currentElement.setLength(0); // Clear the current element
                } else {
                    currentElement.append(c);
                }
            }
            result.add(currentElement.toString().trim()); // Add the last element
            for(int i = 1; i < result.size(); i++) {
                e.add(stringToExpression(result.get(i)));
            }
            return createExpression(result.get(0),e);
        }
    }

    private Expression createExpression(String operator, List<Expression> args) {
        return switch (operator.trim()) {
            case "PLUS" -> new Plus(args.get(0), args.get(1));
            case "MINUS" -> new Minus(args.get(0), args.get(1));
            case "TIMES" -> new Times(args.get(0), args.get(1));
            case "DIVIDE" -> new Divide(args.get(0), args.get(1));
            case "MOD" -> new Modulo(args.get(0), args.get(1));
            case "POW" -> new Pow(args.get(0), args.get(1));
            case "ABS" -> new AbsoluteValue(args.get(0));
            case "CONCAT" -> new Concat(args.get(0), args.get(1));
            case "SUB" -> new Sub(args.get(0), args.get(1),args.get(2));
            case "REF" -> new Reference(args.get(0));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}

