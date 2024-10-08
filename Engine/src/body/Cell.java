package body;

import body.impl.Coordinate;
import expression.api.EffectiveValue;
import expression.api.Expression;

import java.io.IOException;
import java.util.List;

public interface Cell {
    String getId();
    void setId(String id);
    int getLastVersionUpdate();
    void setLastVersionUpdate(int version);
    String getOriginalValue();
    void setOriginalValue(String original);
    EffectiveValue getEffectiveValue();
    Expression getExpression();
    List<Coordinate> getCellsDependsOnThem();
    List<Coordinate> getCellsDependsOnHim();
    Coordinate getCoordinate();
    void setExpression(Expression expression);
    void setEffectiveValue(EffectiveValue value);
    void setDependsOnThem(List<Coordinate> dependsOnThem);
    void setDependsOnHim(List<Coordinate> dependsOnHim);
    Cell copyCell() throws IOException, ClassNotFoundException;
}
