package expression;

import expression.impl.Empty;
import expression.Range;

public enum CellType {
    NUMERIC(Double.class) ,
    STRING(String.class) ,
    BOOLEAN(Boolean.class) ,
    EMPTY(Empty .class) ,
    RANGE(Range .class) ;

    private Class<?> type;

    CellType(Class<?> type) {
        this.type = type;
    }

    public boolean isAssignableFrom(Class<?> aType) {
        return type.isAssignableFrom(aType);
    }
}
