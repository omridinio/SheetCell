package body.impl;

import body.Cell;
import method.expression.Expression;

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
    }

    @Override
    public Object getEffectivelValue() {return effectiveValue.evaluate();
    }
    //TODO : creat function that get string(from original value) and convert it to expression
    @Override
    public void setEffectivelValue(String effective) {
        String[] words = originalValue.split(",");
    }

    @Override
    public List<Cell> getCellsDependsOnThem() {
        return getCellsDependsOnThem();
    }

//    private Boolean checkBracketValidetoin(String expression){
//
//    }
}
