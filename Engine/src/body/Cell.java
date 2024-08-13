package body;

import method.expression.Expression;

import java.util.List;

public interface Cell {
    String getId();
    void setId(String id);
    int getLastVersionUpdate();
    void setLastVersionUpdate(int version);
    String getOriginalValue();
    void setOriginalValue(String original);
    Object getEffectiveValue();
    public Expression getExpression();
    List<Cell> getCellsDependsOnThem();

}
