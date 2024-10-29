package dto.impl;

//import body.Cell;

import java.util.ArrayList;
import java.util.List;

public class CellDTO {
    final private String Id;
    final private int lastVersionUpdate;
    final private String originalValue;
    final private String effectiveValue;
    final private String lastUserUpdate;
    final private List<Coordinate> cellsDependsOnThem = new ArrayList<>();
    final private List<Coordinate> cellsDependsOnHim = new ArrayList<>();


//    public CellDTO(Cell cell) {
//        this.Id = cell.getId();
//        this.lastVersionUpdate = cell.getLastVersionUpdate();
//        this.originalValue = cell.getOriginalValue();
//        this.effectiveValue = cell.getEffectiveValue().toString();
//        this.lastUserUpdate = cell.getUsername();
//        createListDepends(cell.getCellsDependsOnThem(), this.cellsDependsOnThem);
//        createListDepends(cell.getCellsDependsOnHim(), this.cellsDependsOnHim);
//    }

    public CellDTO(String Id, int lastVersionUpdate, String originalValue, String effectiveValue, String lastUserUpdate, List<Coordinate> cellsDependsOnThem, List<Coordinate> cellsDependsOnHim) {
        this.Id = Id;
        this.lastVersionUpdate = lastVersionUpdate;
        this.originalValue = originalValue;
        this.effectiveValue = effectiveValue;
        this.lastUserUpdate = lastUserUpdate;
        createListDepends(cellsDependsOnThem, this.cellsDependsOnThem);
        createListDepends(cellsDependsOnHim, this.cellsDependsOnHim);
    }

    private void createListDepends(List<Coordinate> from, List<Coordinate> to) {
        to.addAll(from);
    }

    public String getId() {
        return Id;
    }
    public int getLastVersionUpdate() {
        return lastVersionUpdate;
    }
    public String getOriginalValue() {return originalValue;}
    public String getOriginalEffectiveValue() {return effectiveValue;}
    public List<Coordinate> getCellsDependsOnThem() {return cellsDependsOnThem;}
    public List<Coordinate> getCellsDependsOnHim() {return cellsDependsOnHim;}
    public String getLastUserUpdate() { return lastUserUpdate; }
}
