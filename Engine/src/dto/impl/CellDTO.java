package dto.impl;

import body.Cell;
import body.Coordinate;
import expression.api.EffectiveValue;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private List<Coordinate> cellsDependsOnThem = new ArrayList<>();
    private List<Coordinate> cellsDependsOnHim =new ArrayList<>();

    public CellDTO(String id, int lastVersionUpdate, String originalValue, EffectiveValue effectiveValue,List<Coordinate> cellsDependsOnThem, List<Coordinate> cellsDependsOnHim) {
        this.Id = id;
        this.lastVersionUpdate = lastVersionUpdate;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.cellsDependsOnThem = cellsDependsOnThem;
        this.cellsDependsOnHim = cellsDependsOnHim;
    }
    public CellDTO(Cell cell) {
        this.Id = cell.getId();
        this.lastVersionUpdate = cell.getLastVersionUpdate();
        this.originalValue = cell.getOriginalValue();
        this.effectiveValue = cell.getEffectiveValue();
        this.cellsDependsOnThem = cell.getCellsDependsOnThem();
        this.cellsDependsOnHim = cell.getCellsDependsOnHim();

    }
    public String getId() {
        return Id;
    }
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }
    public String getOriginalValue() {return originalValue;}
    public Object getEffectiveValue() {return effectiveValue.getValue();}
    public Object getOriginalEffectiveValue() {return effectiveValue;}
    public List<Coordinate> getCellsDependsOnThem() {return cellsDependsOnThem;}
    public List<Coordinate> getCellsDependsOnHim() {return cellsDependsOnHim;}
}
