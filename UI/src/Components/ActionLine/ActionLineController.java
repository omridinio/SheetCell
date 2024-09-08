package Components.ActionLine;

import Components.Shitcell.ShitsellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ActionLineController {

    @FXML
    private Label actionError;

    @FXML
    private TextField actionLine;

    @FXML
    private TextField cellId;

    @FXML
    private TextField lastVersion;

    @FXML
    private TextField originalValue;

    @FXML
    private Button updateValue;

    @FXML
    private ComboBox<?> versionSelctor;

    private ShitsellController shitsellController;

    public void initialize() {}

    public void initializeActionLine() {
        shitsellController.intitializeActionLine(this);
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public Label getActionError() {
        return actionError;
    }

    public TextField getActionLine() {
        return actionLine;
    }

    public TextField getCellId() {
        return cellId;
    }

    public TextField getLastVersion() {
        return lastVersion;
    }

    public TextField getOriginalValue() {
        return originalValue;
    }

    public Button getUpdateValue() {
        return updateValue;
    }

    public ComboBox<?> getVersionSelctor() {
        return versionSelctor;
    }

    @FXML
    void cellIdEnter(ActionEvent event) {
        shitsellController.cellIdEnter(cellId);
    }

    @FXML
    void updateCellClicked(ActionEvent event) throws IOException {
        shitsellController.updateCellClicked(actionLine, cellId);
    }

}
