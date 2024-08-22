package body.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.Number;
import expression.impl.Str;
import expression.impl.numeric.*;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.REF;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.*;
import expression.impl.Reference;

public class ImplSheet implements Sheet,Serializable  {



    private int sheetVersion = 1;
    final private String sheetName;
    final private int thickness;
    final private int width;
    final private int row;
    final private int col;
    private Map<Coordinate, Cell> activeCells = new HashMap<>();
    private Graph graph;
    private int countUpdateCell = 0;

    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        if (row > 50 || col > 20 || row < 1 || col < 1) {
            throw new IllegalArgumentException("The row or the column is out of bounds, Please try again.");
        }
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
        this.graph = new Graph();
    }

    public ImplSheet(ImplSheet other) {
        this.sheetName = other.sheetName; // Strings are immutable, so this is safe
        this.thickness = other.thickness;
        this.width = other.width;
        this.row = other.row;
        this.col = other.col;
        this.sheetVersion = other.sheetVersion;

        // Deep copy of activeCells
        this.activeCells = new HashMap<>();
        for (Map.Entry<Coordinate, Cell> entry : other.activeCells.entrySet()) {
            this.activeCells.put(entry.getKey(), new ImplCell((ImplCell) entry.getValue()));
        }

        // Deep copy of graph
        this.graph = new Graph(other.graph); // Assuming Graph has a copy constructor
    }


    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellID) {
        Coordinate coordinate = new CoordinateImpl(cellID);
        checkValidBounds(coordinate);
//        if(coordinate.getRow() > row || coordinate.getColumn() > col){
//            throw new IllegalArgumentException("Cell is out of bounds");
//        }
        if(!activeCells.containsKey(coordinate)){
            return new ImplCell(cellID);
//            activeCells.put(coordinate, new ImplCell(cellID));
//            graph.addVertex(coordinate);
        }
        else {
            updateListsOfDependencies(coordinate);
            return activeCells.get(coordinate);
        }
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

    private void checkValidBounds(Coordinate coordinate) {
        if(coordinate.getRow() > row || coordinate.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
    }

    @Override
    public void updateCellDitels(String cellId, String value){
        Coordinate currCoord = new CoordinateImpl(cellId);
        checkValidBounds(currCoord);
//        if(currCoord.getRow() > row || currCoord.getColumn() > col){
//            throw new IllegalArgumentException("Cell is out of bounds");
//        }

//        if(!activeCells.containsKey(currCoord)){
        activeCells.putIfAbsent(currCoord, new ImplCell(cellId));
        graph.addVertex(currCoord);
//        }

        sheetVersion = sheetVersion + 1;
        graph.removeEntryEdges(currCoord);
        Cell cell = activeCells.get(currCoord);
        cell.setOriginalValue(value);
        //why we need it?
        cell.setEffectiveValue(null);
        Expression currExpression = stringToExpression(value,currCoord);
        cell.setExpression(currExpression);
        cell.setLastVersionUpdate(sheetVersion);
        countUpdateCell++;
    }

    //TODO: omri need to explain to me the REF loop.
    @Override
    public void updateCellEffectiveValue(){
        List<Coordinate> topologicalSorted = graph.topologicalSort();
        for(Coordinate coord : topologicalSorted){
            Cell currCell = activeCells.get(coord);
            String value = currCell.getOriginalValue();
            Expression currExpression = stringToExpression(value,coord);
            currCell.setExpression(currExpression);
//            if (currCell.getEffectiveValue() instanceof Reference) {
//                Reference ref = (Reference) currCell.getEffectiveValue();
//                ref.setCell(findUpdateCell(ref.getCell()));
//                currCell.setExpression(ref);
//                if(ref.getCell().getLastVersionUpdate() > currCell.getLastVersionUpdate()){
//                    countUpdateCell++;
//                }
//                currCell.setLastVersionUpdate(ref.getCell().getLastVersionUpdate());
//            }
            currCell.setEffectiveValue(currCell.getExpression().evaluate());
        }
    }

    @Override
    public void updateCell(String cellId, String value) {
//        Coordinate currCoord = new CoordinateImpl(cellId);
//        if(currCoord.getRow() > row || currCoord.getColumn() > col){
//            throw new IllegalArgumentException("Cell is out of bounds");
//        }
//        if(!activeCells.containsKey(currCoord)){
//            activeCells.put(currCoord, new ImplCell(cellId));
//            graph.addVertex(currCoord);
//        }
//        graph.removeEntryEdges(currCoord);
//        Cell cell = activeCells.get(currCoord);
//        cell.setOriginalValue(value);
//        Expression currExpression= stringToExpression(value,currCoord);
//        cell.setExpression(currExpression);
//        cell.setLastVersionUpdate(sheetVersion);
        updateCellDitels(cellId, value);
        updateCellEffectiveValue();
//      List<Coordinate> res = graph.topologicalSort();
//        for(Coordinate coord : res){
//            Cell currCell = activeCells.get(coord);
//
//            if (currCell.getEffectiveValue() instanceof Reference) {
//                Reference ref = (Reference) currCell.getEffectiveValue();
//                ref.setCell(findUpdateCell(ref.getCell()));
//                currCell.setExpression(ref);
//            }
//            currCell.setEffectiveValue(currCell.getExpression().evaluate());
//        }
    }

    private Cell findUpdateCell(Cell prevCell){
        Coordinate coord = new CoordinateImpl(prevCell.getId());
        return activeCells.get(coord);
    }

    @Override
    public void updateListsOfDependencies(Coordinate coord) {
            Cell cell= activeCells.get(coord);
            cell.setDependsOnHim(graph.getNeighbors(coord));
            cell.setDependsOnThem(graph.getSources(coord));
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
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private Expression stringToExpression(String input,Coordinate coordinate) {

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
            if(!isValidOperator(result.get(0).toUpperCase())){
                throw new NumberFormatException("Invalid Operator" + System.lineSeparator() + "The valid Operator are: PLUS, MINUS, TIMES, DIVIDE, MOD, POW, CONCAT, ABS, SUB, REF");
            }
            isValidNumOfArgs(result);
            for(int i = 1; i < result.size(); i++) {
                e.add(stringToExpression(result.get(i),coordinate));
            }

            return createExpression(result.get(0),e, coordinate);
        }
    }

    private Boolean isValidNumOfArgs(List<String> args){
        Boolean res = true;
        switch (args.get(0).toUpperCase()) {
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
                if (args.size() == 2){
                    args.set(0, "REF");
                    args.set(1, args.get(1).toUpperCase());
                }
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

    private Expression createExpression(String operator, List<Expression> args, Coordinate coordinate) {
        return switch (operator.trim().toUpperCase()) {
            case "PLUS" -> new Plus(args.get(0), args.get(1));
            case "MINUS" -> new Minus(args.get(0), args.get(1));
            case "TIMES" -> new Times(args.get(0), args.get(1));
            case "DIVIDE" -> new Divide(args.get(0), args.get(1));
            case "MOD" -> new Modulo(args.get(0), args.get(1));
            case "POW" -> new Pow(args.get(0), args.get(1));
            case "ABS" -> new AbsoluteValue(args.get(0));
            case "CONCAT" -> new Concat(args.get(0), args.get(1));
            case "SUB" -> new Sub(args.get(0), args.get(1),args.get(2));
            case "REF" -> new REF(args.get(0), activeCells.get(refHelper(args.get(0), coordinate)));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private Coordinate refHelper(Expression input, Coordinate toCoordinate){
        String cellID = (String) input.evaluate().getValue();
        if(validInputCell(cellID.toUpperCase(), toCoordinate)){
            Coordinate coordinate = new CoordinateImpl(cellID);
            return coordinate;
        }
        return null;
    }

    @Override
    public int getCountUpdateCell() {
        return countUpdateCell;
    }

    private boolean validInputCell(String input, Coordinate toCoordinate){
        if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {
            String temp = input.substring(1);
            try {
                Coordinate coordinate = new CoordinateImpl(input.toUpperCase());
                if(coordinate.getRow() > row || coordinate.getColumn() > col){
                    throw new IllegalArgumentException("Cell is out of bounds");
                }
                if(!activeCells.containsKey(coordinate)){
                    throw new IllegalArgumentException("Cell is not exist");
                }
                //check if create a circle
                graph.addEdge(coordinate, toCoordinate);
                if(graph.hasCycle()){
                    graph.removeEdge(coordinate, toCoordinate);
                    throw new IllegalArgumentException("Error: the cell: " + input + "create a circle");
                }


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
