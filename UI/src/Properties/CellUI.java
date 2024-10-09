package Properties;

import Components.Cell.CellContoller;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.scene.control.Button;

public class CellUI {
    public StringProperty cellid = new SimpleStringProperty("");
    public StringProperty lastUserUpdate = new SimpleStringProperty("");
    public StringProperty originalValue = new SimpleStringProperty("");
    public StringProperty lastVersion = new SimpleStringProperty("");
    public Button clickedCell = new Button();
    public BooleanProperty isClicked = new SimpleBooleanProperty(false);
    public CellContoller cellContoller = null;
    public BooleanProperty errorUpdate = new SimpleBooleanProperty(false);
    public IntegerProperty textFontSize = new SimpleIntegerProperty(12);
    public StringProperty textFontType = new SimpleStringProperty("Ariel");
    public StringProperty textFontColor = new SimpleStringProperty("000000");
    public StringProperty backgroundColor = new SimpleStringProperty("fffefe");

}
