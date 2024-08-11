package body;

import java.util.List;

public interface Cell {
    String getId();
    void setId(String id);
    int getLastVersionUpdate();
    void setLastVersionUpdate(int version);
    String getOriginalValue();
    void setOriginalValue(String original);
    String getEffectivelValue();
    void setEffectivelValue(String effective);
    List<Cell> getCellsDependsOnThem();

}
