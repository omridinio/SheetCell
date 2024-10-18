package Components.ActionLine;

import Components.Error.ErrorController;
import Components.Shitcell.ShitsellController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
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
    private TextField userName;

    @FXML
    private TextField originalValue;

    @FXML
    private Button updateValue;

    @FXML
    private ComboBox<Integer> versionSelctor;

    private ShitsellController shitsellController;

    public SimpleBooleanProperty isUpdate = new SimpleBooleanProperty();

    public void initialize() {
        versionSelctor.setDisable(false);
        versionSelctor.getItems().add(1);
        versionSelctor.setValue(versionSelctor.getItems().getLast());
        versionSelctor.setEditable(false);
        isUpdate.bind(Bindings.createBooleanBinding(
                () -> {
                    return versionSelctor.getValue() != null
                            && !versionSelctor.getItems().isEmpty()
                            && versionSelctor.getValue().equals(versionSelctor.getItems().get(versionSelctor.getItems().size() - 1));
                },
                versionSelctor.valueProperty(),
                versionSelctor.getItems()
        ));
    }

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

    public TextField getUserName() {
        return userName;
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

    public ComboBox<Integer> getVersionSelctor() {
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

    public void addVersion(){
        versionSelctor.getItems().add(versionSelctor.getItems().size() + 1);
        versionSelctor.setValue(versionSelctor.getItems().getLast());
    }

    public void addNewVersions(int lastVersion) {
        for(int i = versionSelctor.getItems().getLast() + 1; i <= lastVersion; i++) {
            versionSelctor.getItems().add(i);
        }
    }


    public void rest() {
        actionLine.clear();
        cellId.clear();
        lastVersion.clear();
        originalValue.clear();
        versionSelctor.getItems().clear();
        versionSelctor.getItems().add(1);
        versionSelctor.setValue(versionSelctor.getItems().getLast());
        versionSelctor.valueProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            try {
                if (newValue != null) {
                    if(newValue != versionSelctor.getItems().getLast()) {
                        shitsellController.versionSelected(newValue);
                    }
                    else {
                        shitsellController.getUpdateSheetVersion();
                    }
                }
            } catch (IOException e) {
                ErrorController.showError(e.getMessage());
            }
        });
    }
}
