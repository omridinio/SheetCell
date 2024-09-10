package body.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import dto.impl.CellDTO;
import dto.impl.RangeDTO;
import expression.Range;
import expression.api.EffectiveValue;
import expression.api.Expression;
import expression.impl.*;
import expression.impl.Number;
import expression.impl.bool.*;
import expression.impl.numeric.*;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.REF;

import java.io.IOException;
import java.io.Serializable;
import java.security.PublicKey;
import java.util.*;

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
    private Map<String, Range> activeRanges = new HashMap<>();

    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        if (row > 50 || col > 20 || row < 1 || col < 1) {
            throw new IllegalArgumentException("ERROR! Can't load the file. The file has a row or column that is out of range.");
        }
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
        this.graph = new Graph();
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellID) {
        Coordinate coordinate = new CoordinateImpl(cellID);
        checkValidBounds(coordinate);
        if(!activeCells.containsKey(coordinate)){
            return new ImplCell(cellID);
        }
        else {
            updateListsOfDependencies(coordinate);
            return activeCells.get(coordinate);
        }
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        checkValidBounds(coordinate);
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

    @Override
    public void setUpdateCellCount(int countUpdateCell) {
        this.countUpdateCell = countUpdateCell;
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
        activeCells.putIfAbsent(currCoord, new ImplCell(cellId));
        graph.addVertex(currCoord);
        sheetVersion = sheetVersion + 1;
        graph.removeEntryEdges(currCoord);
        Cell cell = activeCells.get(currCoord);
        cell.setOriginalValue(value);

        //why we need it?
        cell.setEffectiveValue(null);
        Expression currExpression = stringToExpression(value,currCoord);
        cell.setExpression(currExpression);
        cell.setOriginalValue(currExpression.expressionTOtoString());
        cell.setLastVersionUpdate(sheetVersion);
        countUpdateCell++;
    }

    @Override
    public void updateCellEffectiveValue(String cellId){
        Set<Coordinate> neighbors = graph.listOfAccessibleVertex(new CoordinateImpl(cellId));
        List<Coordinate> topologicalSorted = graph.topologicalSort();

        for(Coordinate coord : topologicalSorted){
            Cell currCell = activeCells.get(coord);
            if(neighbors.contains(coord) && !coord.equals(new CoordinateImpl(cellId))){
                currCell.setLastVersionUpdate(sheetVersion);
                countUpdateCell++;
            }
            String value = currCell.getOriginalValue();
            Expression currExpression = stringToExpression(value,coord);
            currCell.setExpression(currExpression);
            currCell.setEffectiveValue(currCell.getExpression().evaluate());
            currCell.setOriginalValue(currExpression.expressionTOtoString());
        }
    }

    @Override
    public void updateCell(String cellId, String value) {
        resrRanges();
        updateCellDitels(cellId, value);
        updateCellEffectiveValue(cellId);
    }

    private void resrRanges() {
        for (Range range : activeRanges.values()) {
            range.restUse();
        }
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
        Expression currExpression = helperStringToExpression(input,coordinate);
        if(currExpression instanceof Str ){
            return new Str(((Str) currExpression).getValue().toString().trim());
        }
        return currExpression;
    }

    private Expression helperStringToExpression(String input,Coordinate coordinate) {

        if(input.isEmpty()){
            return new Empty();
        }
        validInputBracket(input);
        //if(!input.contains(",")){
        if(!(input.trim().charAt(0) == '{' && input.trim().charAt(input.trim().length()-1) == '}')){
            try{
                Double.parseDouble(input);
                return (new Number(input));
            }catch (NumberFormatException error){
                if(input.toUpperCase().equals("TRUE")) {
                    return (new Bool(true));
                }
                else if(input.toUpperCase().equals("FALSE")) {
                    return (new Bool(false));
                }
                return (new Str(input));
            }
        }

        else {
            input = input.trim();
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
                    if(!currentElement.toString().isEmpty()){
                        result.add(currentElement.toString());
                    }

                    currentElement.setLength(0); // Clear the current element
                } else {
                    currentElement.append(c);
                }
            }
            if(!currentElement.toString().isEmpty()){
                result.add(currentElement.toString()); // Add the last element
            }
            if(!isValidOperator(result.get(0).toUpperCase().trim())){
                throw new NumberFormatException("Invalid Operator" + System.lineSeparator() + "The valid Operator are: PLUS, MINUS, TIMES, DIVIDE, MOD, POW, CONCAT, ABS, SUB, REF");
            }
            isValidNumOfArgs(result);
            for(int i = 1; i < result.size(); i++) {
                e.add(helperStringToExpression(result.get(i),coordinate));
            }

            return createExpression(result.get(0), e, coordinate);
        }
    }

    private Boolean isValidNumOfArgs(List<String> args){
        Boolean res = true;
        switch (args.get(0).toUpperCase().trim()) {
            case "PLUS":
            case "MINUS":
            case "TIMES":
            case "DIVIDE":
            case "MOD":
            case "POW":
            case "PERCENT":
            case "EQUAL":
            case "BIGGER":
            case "LESS":
            case "OR":
            case "AND":
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
            case "SUM":
            case "AVERAGE":
            case "NOT":
                if (args.size() != 2){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 1 arguments.");
                }
                break;
            case "SUB":
            case "IF":
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
            case "PERCENT" -> new Percent(args.get(0), args.get(1));
            case "EQUAL" -> new Equal(args.get(0), args.get(1));
            case "BIGGER" -> new Bigger(args.get(0), args.get(1));
            case "LESS" -> new Less(args.get(0), args.get(1));
            case "OR" -> new Or(args.get(0), args.get(1));
            case "AND" -> new And(args.get(0), args.get(1));
            case "NOT" -> new Not(args.get(0));
            case "IF" -> new If(args.get(0), args.get(1), args.get(2));
            case "SUM" -> {
                if(activeRanges.containsKey(args.get(0).evaluate().getValue().toString())) {
                    rangeHelper(coordinate, activeRanges.get(args.get(0).evaluate().getValue().toString()));
                    yield new Sum(activeRanges.get(args.get(0).evaluate().getValue().toString()));
                }
                else{
                    throw new IllegalArgumentException("Range is not exist");
                }
            }
            case "AVERAGE" -> {
                if(activeRanges.containsKey(args.get(0).evaluate().getValue().toString())) {
                    rangeHelper(coordinate, activeRanges.get(args.get(0).evaluate().getValue().toString()));
                    yield new Average(activeRanges.get(args.get(0).evaluate().getValue().toString()));
                }
                else{
                    throw new IllegalArgumentException("Range is not exist");
                }
            }
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private Coordinate refHelper(Expression input, Coordinate toCoordinate){
        String cellID = (String) input.evaluate().getValue();
        cellID = cellID.trim();
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
                    activeCells.put(coordinate, new ImplCell(input));
                    //throw new IllegalArgumentException("Cell is not exist");
                }
                //check if create a circle
                graph.addEdge(coordinate, toCoordinate);
                if(graph.hasCycle()){
                    graph.removeEdge(coordinate, toCoordinate);
                    throw new IllegalArgumentException("Error! the cell: " + input + " create a circle");
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
            case "PLUS", "MINUS", "TIMES", "DIVIDE", "MOD", "POW", "CONCAT", "ABS", "SUB", "REF", "PERCENT", "EQUAL", "BIGGER", "LESS", "OR", "AND", "NOT", "IF", "SUM", "AVERAGE" -> true;
            default -> false;
        };
    }

    @Override
    public void addNewRange(String rangeId, String cellRange) {
        if(activeRanges.containsKey(rangeId)){
            throw new IllegalArgumentException("Range is already exist");
        }
        activeRanges.put(rangeId, CreateRange(rangeId, cellRange));
    }

    private Range CreateRange(String rangeId, String cellRange){
        List<Coordinate> coordinateRange = getCoordinateInRange(cellRange);
        List<Cell> cellInRange = new ArrayList<>();
        for(Coordinate coord : coordinateRange){
            activeCells.putIfAbsent(coord, new ImplCell(coord.toString()));
            graph.addVertex(coord);
            cellInRange.add(activeCells.get(coord));
        }
        Range range = new Range(cellInRange, rangeId);
        Coordinate from = coordinateRange.get(0);
        Coordinate to = coordinateRange.get(coordinateRange.size()-1);
        range.setFrom(from);
        range.setTo(to);
        return range;
    }

    private List<Coordinate> getCoordinateInRange(String cellRange) {
        String [] range = cellRange.split("\\.\\.");
        Coordinate firstCell = createCoordinate(range[0]);
        Coordinate lastCell = createCoordinate(range[1]);
        List<Coordinate> cellsRange = new ArrayList<>();
        int firstRow = firstCell.getRow();
        int lasrRow = lastCell.getRow();
        int firstCol = firstCell.getColumn();
        int lastCol = lastCell.getColumn();
        if(firstRow > lasrRow || firstCol > lastCol){
            throw new IllegalArgumentException("Invalid range");
        }
        for(int i = firstRow; i <= lasrRow; i++){
            for(int j = firstCol; j <= lastCol; j++){
                Coordinate coord = new CoordinateImpl(i,j);
                cellsRange.add(coord);
            }
        }
        return cellsRange;
    }

    @Override
    public List<Integer> getTheRangeOfTheRange(String cellRange) {
        String [] range = cellRange.split("\\.\\.");
        Coordinate firstCell = createCoordinate(range[0]);
        Coordinate lastCell = createCoordinate(range[1]);
        List<Coordinate> cellsRange = new ArrayList<>();
        int firstRow = firstCell.getRow();
        int lasrRow = lastCell.getRow();
        int firstCol = firstCell.getColumn();
        int lastCol = lastCell.getColumn();
        if(firstRow > lasrRow || firstCol > lastCol){
            throw new IllegalArgumentException("Invalid range");
        }
        List<Integer> res = new ArrayList<>();
        res.add(firstCol);
        res.add(lastCol);
        return res;
    }

    private Coordinate createCoordinate(String cellID){
        Coordinate coord = new CoordinateImpl(cellID);
        if(coord.getRow() > row || coord.getColumn() > col){
            throw new IllegalArgumentException("Cell is out of bounds");
        }
        return coord;
    }

    @Override
    public Range getRange(String rangeId){
        return activeRanges.get(rangeId);
    }

    private void rangeHelper(Coordinate currCoord, Range currRange){
        List<Cell> rangeCells = currRange.getRangeCells();
        for(Cell cell : rangeCells){
            Coordinate coord = new CoordinateImpl(cell.getId());
            graph.addVertex(coord);
            graph.addEdge(coord, currCoord);
        }
        if (graph.hasCycle()) {
            for (Cell cell : rangeCells) {
                Coordinate coord = new CoordinateImpl(cell.getId());
                graph.removeEdge(coord, currCoord);
            }
            throw new IllegalArgumentException("Error! the range: " + currRange.getRangeId() + " create a circle");
        }
        currRange.addUse();
    }

    @Override
    public void removeRange(String rangeId){
        if(!activeRanges.containsKey(rangeId)){
            throw new IllegalArgumentException("Range is not exist");
        }
        Range currRange = activeRanges.get(rangeId);
        if(currRange.isUse()){
            throw new IllegalArgumentException("Range is in use");
        }
        activeRanges.remove(rangeId);
    }

    @Override
    public List<String> getRangeName() {
        return new ArrayList<>(activeRanges.keySet());
    }

    @Override
    public Map<Coordinate, Cell> sortRange(String cellRange, List<Integer> dominantCol) throws IOException, ClassNotFoundException {
        Range range = CreateRange("Sort Range", cellRange);
        List<Cell> rangeCells = range.getRangeCells();
        Map<Integer,List<Cell>> cols = makeCols(range);
        Map<Coordinate, Cell> sortCells = createSortedCells(rangeCells);
        int colLength = cols.get(dominantCol.get(0)).size();
        int startCol = range.getFrom().getColumn();
        int endCol = range.getTo().getColumn();
        int startRow = range.getFrom().getRow();
        int endRow = range.getTo().getRow();
        for (int i = 0; i < colLength; i++){
            for (int j = 0; j < colLength - i - 1 ; j++){
                if (compareUntilTheEnd(cols, dominantCol, j, j + 1) > 0){
                    switchRows(j + startRow, j + 1 + startRow, sortCells, startCol, endCol);
                    switchCols(cols, j, j + 1);
                }
            }
        }
        return sortCells;
    }

    private void switchCols(Map<Integer, List<Cell>> cols, int firstRow, int lastRow) {
        for (int col : cols.keySet()) {
            List<Cell> currCol = cols.get(col);
            Cell temp = currCol.get(firstRow);
            currCol.set(firstRow, currCol.get(lastRow));
            currCol.set(lastRow, temp);
        }
    }

    private Map<Coordinate, Cell> createSortedCells(List<Cell> rangeCells) throws IOException, ClassNotFoundException {
        Map<Coordinate, Cell> sortCells = new HashMap<>();
        for (Cell cell : rangeCells){
            Coordinate coordinate = new CoordinateImpl(cell.getId());
            sortCells.put(coordinate, cell.copyCell());
        }
        return sortCells;
    }

    private void switchRows(int firstRow, int lastRow, Map<Coordinate, Cell> sortCells, int stratRow, int endRow) throws IOException, ClassNotFoundException {
        for (int i = stratRow; i <= endRow; i++){
            Coordinate firstCoord = new CoordinateImpl(firstRow, i);
            Coordinate lastCoord = new CoordinateImpl(lastRow, i);
            Cell firstCell = sortCells.get(firstCoord).copyCell();
            Cell lastCell = sortCells.get(lastCoord).copyCell();
            sortCells.put(firstCoord, lastCell);
            sortCells.put(lastCoord, firstCell);
        }
    }

    private Map<Integer,List<Cell>> makeCols(Range range) {
        Map<Integer,List<Cell>> cols = new HashMap<>();
        String firstCol = range.getFrom().toString();
        String lastCol = range.getTo().toString();
        int firstcol = firstCol.charAt(0) - 'A' + 1;
        int lastcol = lastCol.charAt(0) - 'A' + 1;
        int firstrow = Integer.parseInt(firstCol.substring(1));
        int lastrow = Integer.parseInt(lastCol.substring(1));
        for (int i = firstcol; i <= lastcol; i++) {
            cols.put(i, new ArrayList<>());
            for (int j = firstrow; j <= lastrow; j++) {
                Coordinate coordinate = new CoordinateImpl(j, i);
                activeCells.putIfAbsent(coordinate, new ImplCell(coordinate.toString()));
                Cell cell = activeCells.get(coordinate);
                cols.get(i).add(cell);
            }
        }
        return cols;
    }

    public double compare(String a, String b) {
        if (tryParseDouble(a) && tryParseDouble(b)) {
            return Double.parseDouble(a) - Double.parseDouble(b);
        }
        else if (!tryParseDouble(a) && !tryParseDouble(b)) {
            return a.compareTo(b);
        }
        else if (tryParseDouble(a)) {
            return 1;
        }
        else {
            return -1;
        }

    }

    public double compareUntilTheEnd(Map<Integer,List<Cell>> cols, List<Integer> dominantCol, int line1, int line2){
        double res = 0;
        int index = 0;
        List<Cell> col = cols.get(dominantCol.get(index));
        while(res == 0) {
            res = compare(col.get(line1).getEffectiveValue().toString(), col.get(line2).getEffectiveValue().toString());
            if (res == 0) {
                index++;
                if (index == dominantCol.size()) {
                    break;
                }
                col = cols.get(dominantCol.get(index));
            }
        }

        return res;
    }

    private boolean tryParseDouble(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }



}
