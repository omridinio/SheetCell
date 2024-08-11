package body.impl;

import body.Cell;

import java.util.ArrayList;
import java.util.List;

public class ImplCell implements Cell {

    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private String effectiveValue;
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
    public String getEffectivelValue() {
        return effectiveValue;
    }

    @Override
    public void setEffectivelValue(String effective) {
        effectiveValue = effective;
    }

    @Override
    public List<Cell> getCellsDependsOnThem() {
        return getCellsDependsOnThem();
    }

}
