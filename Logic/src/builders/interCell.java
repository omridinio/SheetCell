package builders;

import builders.impl.Cell;

import java.util.List;

public interface interCell {
    String getId();
    void setId(String id);
    int getLastVersionUpdate();
    void setLastVersionUpdate(int version);
    String getOriginalValue();
    void setOriginalValue(String original);
    String getEffectivelValue();
    void setEffectivelValue(String effective);
    List<interCell> getCellsDependsOnThem();

}
