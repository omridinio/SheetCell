package Properties;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.scene.control.Button;

public class CellUI {
    public StringProperty cellid = new SimpleStringProperty("");
    public StringProperty originalValue = new SimpleStringProperty("");
    public StringProperty lastVersion = new SimpleStringProperty("");
    public Button clickedCell = new Button();
    public BooleanProperty isClicked = new SimpleBooleanProperty(false);
}
