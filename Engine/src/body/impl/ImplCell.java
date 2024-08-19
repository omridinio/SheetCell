package body.impl;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.*;
import expression.impl.Number;
import expression.impl.numeric.*;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.Reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ImplCell implements Cell {

    private Coordinate coor;
    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private Expression effectiveValue;
    private EffectiveValue effectiveValue1;
    private List<Cell> cellsDependsOnThem = new ArrayList<>();
    private List<Cell> cellsDependsOnHim = new ArrayList<>();

    public ImplCell(String id) {
        Id = id;
        lastVersionUpdate = 1;
        originalValue = "";
    }

    public ImplCell(double num){
        Number number = new Number(num);
        effectiveValue1 = number;
    }

    public ImplCell(Cell cell) {
        Id = cell.getId();
        lastVersionUpdate = cell.getLastVersionUpdate();
        originalValue = cell.getOriginalValue();
        effectiveValue = cell.getExpression();
        effectiveValue1 = cell.getEffectiveValue();
        cellsDependsOnThem = cell.getCellsDependsOnThem();
        coor = cell.getCoordinate();
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
        effectiveValue1 = effectiveValue.evaluate();
    }

    @Override
    public EffectiveValue getEffectiveValue()throws NumberFormatException {return effectiveValue1;}

    @Override
    public List<Cell> getCellsDependsOnThem() {
        return cellsDependsOnThem;
    }

    @Override
    public Coordinate getCoordinate() {
        return coor;
    }

    @Override
    public Expression getExpression() {
        return effectiveValue;
    }

    private void validInputBracket(String input){
        if(input.charAt(0) == '{') {
            if(!isValidBracket(input)){
                throw new NumberFormatException("Invalid expression Bracket");
            }
        }

    }
    private boolean isValidBracket(String s) {

        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '{') {
                stack.push(c);
            } else if (c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char openBracket = stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private Expression stringToExpression(String input) {

        if(input.isEmpty()){
            return null;
        }
        validInputBracket(input);
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
            if(!isValidOperator(result.get(0))){
                throw new NumberFormatException("Invalid Operator" + System.lineSeparator() + "The valid Operator are: PLUS, MINUS, TIMES, DIVIDE, MOD, POW, CONCAT, ABS, SUB, REF");
            }
            isValidNumOfArgs(result);
            for(int i = 1; i < result.size(); i++) {
                e.add(stringToExpression(result.get(i)));
            }

            return createExpression(result.get(0),e);
        }
    }

    private Boolean isValidNumOfArgs(List<String> args){
        Boolean res = true;
        switch (args.get(0)) {
            case "PLUS":
            case "MINUS":
            case "TIMES":
            case "DIVIDE":
            case "MOD":
            case "POW":
            case "CONCAT":
                if (args.size() != 3){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 2 arguments.");
                }
                break;
            case "REF":
            case "ABS":
                if (args.size() != 2){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 1 arguments.");
                }
                break;
            case "SUB":
                if (args.size() != 4){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 3 arguments.");
                }
                break;
            default:
                res = true;
                break;
        }
        return res;
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

    private boolean isValidOperator(String operator){
        return switch (operator) {
            case "PLUS", "MINUS", "TIMES", "DIVIDE", "MOD", "POW", "CONCAT", "ABS", "SUB", "REF" -> true;
            default -> false;
        };
    }
}

