package dto;

import body.Cell;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    private String Id;
    private int lastVersionUpdate;
    private String originalValue;
    private EffectiveValue effectiveValue;
    private List<Cell> cellsDependsOnThem = new ArrayList<>();
    private List<Cell> cellsDependsOnHim =new ArrayList<>();

    public CellDTO(String id, int lastVersionUpdate, String originalValue, EffectiveValue effectiveValue,List<Cell> cellsDependsOnThem, List<Cell> cellsDependsOnHim) {
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
        //TODO complete this list
        this.cellsDependsOnHim = null;

    }
    public String getId() {
        return Id;
    }
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }
    public String getOriginalValue() {return originalValue;}
    public Object getEffectiveValue() {return effectiveValue.getValue();}
    public List<Cell> getCellsDependsOnThem() {return cellsDependsOnThem;}
    public List<Cell> getCellsDependsOnHim() {return cellsDependsOnHim;}
}
