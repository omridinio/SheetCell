package expression.api;
import expression.CellType;

public interface EffectiveValue {
    CellType getCellType();
    Object getValue();
    String toString();
    boolean isNaN();
    boolean isUndefined();
    boolean isUnknown();
}
