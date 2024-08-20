package body.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.Str;
import expression.impl.numeric.*;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.Reference;

import java.util.*;

public class ImplSheet implements Sheet {

    private int sheetVersion;
    final private String sheetName;
    final private int thickness;
    final private int width;
    final private int row;
    final private int col;
    private Map<Coordinate, Cell> activeCells = new java.util.HashMap<>();


    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
    }

    public ImplSheet(Sheet sheet) {
        this.sheetName = sheet.getSheetName();
        this.thickness = sheet.getThickness();
        this.width = sheet.getWidth();
        this.row = sheet.getRowCount();
        this.col = sheet.getColumnCount();
        this.sheetVersion = sheet.getVersion();
        Map<Coordinate, Cell> copiedActiveCells = new java.util.HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : this.activeCells.entrySet()) {
            Coordinate copiedCoordinate = new CoordinateImpl(entry.getKey());
            Cell copiedCell = new ImplCell(entry.getValue());
            copiedActiveCells.put(copiedCoordinate, copiedCell);
        }
        this.activeCells = copiedActiveCells;
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellID) {
        Coordinate coordinate = new CoordinateImpl(cellID);
        if(coordinate.getRow() > row || coordinate.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
        if(!activeCells.containsKey(coordinate)){
            activeCells.put(coordinate, new ImplCell(cellID));
        }
        return activeCells.get(coordinate);
    }


    @Override
    public Cell getCell(Coordinate coordinate) {
        if(!activeCells.containsKey(coordinate)){
           return null;
        }
        return activeCells.get(coordinate);
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    @Override
    public int getThickness() {
        return thickness;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public void setVersion(int version) {
        this.sheetVersion = version;
    }

    //TODO check if it work
    @Override
    public void updateCell(String cellId, String value) {
        Coordinate currCoord = new CoordinateImpl(cellId);
        if(currCoord.getRow() > row || currCoord.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
        if(!activeCells.containsKey(currCoord)){
            activeCells.put(currCoord, new ImplCell(cellId));
        }
        Cell cell = activeCells.get(currCoord);
        cell.setOriginalValue(value);
        Expression currExpression= stringToExpression(value);
        cell.setExpression(currExpression);
        cell.setEffectiveValue(currExpression.evaluate());
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
            case "REF" -> new Reference(refHelper(args.get(0)));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private Expression refHelper(Expression input){
        String cellID = (String) input.evaluate().getValue();
        if(validInputCell(cellID)){
            Coordinate coordinate = new CoordinateImpl(cellID);
            return activeCells.get(coordinate).getExpression();
        }
        return null;
    }

    private boolean validInputCell(String input){
        if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {
            String temp = input.substring(1);
            try {
                Coordinate coordinate = new CoordinateImpl(input);
                if(coordinate.getRow() > row || coordinate.getColumn() > col){
                    throw new IllegalArgumentException("Cell is out of bounds");
                }
                if(!activeCells.containsKey(coordinate)){
                    throw new IllegalArgumentException("Cell is not exist");
                }
                //TODO: check if have a circle


            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid input, please enter a valid cell identifier (e.g., A4):");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid input, please enter a valid cell identifier (e.g., A4):");
        }
        return true;
    }


    private boolean isValidOperator(String operator){
        return switch (operator) {
            case "PLUS", "MINUS", "TIMES", "DIVIDE", "MOD", "POW", "CONCAT", "ABS", "SUB", "REF" -> true;
            default -> false;
        };
    }

}
