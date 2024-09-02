package Properties;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CellUI {
    public StringProperty cellid = new SimpleStringProperty("");
    public StringProperty originalValue = new SimpleStringProperty("");
    public StringProperty lastVersion = new SimpleStringProperty("");

}
