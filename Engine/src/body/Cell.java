package body;

import expression.api.EffectiveValue;
import expression.api.Expression;

import java.util.List;

public interface Cell {
    String getId();
    void setId(String id);
    int getLastVersionUpdate();
    void setLastVersionUpdate(int version);
    String getOriginalValue();
    void setOriginalValue(String original);
    EffectiveValue getEffectiveValue();
    public Expression getExpression();
    List<Cell> getCellsDependsOnThem();
    Coordinate getCoordinate();
}
