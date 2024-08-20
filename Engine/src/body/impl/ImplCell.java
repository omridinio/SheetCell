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
    private Expression expression;
    private EffectiveValue effectiveValue;
    private List<Cell> cellsDependsOnThem = new ArrayList<>();
    private List<Cell> cellsDependsOnHim = new ArrayList<>();

    public ImplCell(String id) {
        Id = id;
        lastVersionUpdate = 1;
        originalValue = "";
    }

    public ImplCell(double num){
        Number number = new Number(num);
        effectiveValue = number;
    }

    public ImplCell(Cell cell) {
        Id = cell.getId();
        lastVersionUpdate = cell.getLastVersionUpdate();
        originalValue = cell.getOriginalValue();
        expression = cell.getExpression();
        effectiveValue = cell.getEffectiveValue();
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
        //expression = stringToExpression(originalValue);
        //effectiveValue = expression.evaluate();
    }

    @Override
    public void setEffectiveValue(EffectiveValue value) {
        this.effectiveValue = value;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }
    @Override
    public EffectiveValue getEffectiveValue()throws NumberFormatException {return effectiveValue;}

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
        return expression;
    }


}

