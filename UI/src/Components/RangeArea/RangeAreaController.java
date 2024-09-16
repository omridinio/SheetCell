package Components.RangeArea;

import Components.Error.ErrorController;
import Components.Range.RangeController;
import Components.Range.setRange.setRangeController;
import Components.Shitcell.ShitsellController;
import body.impl.CoordinateImpl;
import dto.impl.CellDTO;
import dto.impl.RangeDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RangeAreaController {

    @FXML
    private FlowPane rangeArea;

    @FXML
    private VBox rangeAreaController;

    @FXML
    private Button addRange;

    @FXML
    private Button deleteRange;

    @FXML
    private Button cancel;

    @FXML
    private Button sumbitDelete;

    private ShitsellController shitsellController;

    private Map<String, RangeController> ranges = new HashMap<>();

    private List<RangeController> selcetedDelete = new ArrayList<>();

    private int countRanges = 0;

    @FXML
    void addRangeClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/setRange/setRange.fxml"));
        Parent newWindowRoot = loader.load();
        setRangeController setRangeController = loader.getController();
        setRangeController.setRangeAreaController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Add new Range");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    @FXML
    void deleteRangeClicked(ActionEvent event) {
        shitsellController.deleteRangeModeOn();
        for (RangeController range : ranges.values()) {
            range.deleteModeOn();
        }
    }

    @FXML
    void deleteSelctedRange(ActionEvent event) throws IOException {
        try {
            for (RangeController range : selcetedDelete) {
                shitsellController.deleteRange(range.getRangeId());
                rangeArea.getChildren().remove(range.getIndex());
                ranges.remove(range);
            }
            for (RangeController range : ranges.values()) {
                range.deleteModeOff();
            }
            selcetedDelete.clear();
            shitsellController.deleteRangeModeOff();
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        shitsellController.deleteRangeModeOff();
        for (RangeController range : ranges.values()) {
            range.deleteModeOff();
        }
        selcetedDelete.clear();
    }

    public void initialize() {

    }

    public void addRange(String rangeId, RangeDTO rangeDTO) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/Range.fxml"));
        Node range = loader.load();
        RangeController rangeContoller = loader.getController();
        rangeContoller.setRangeDTO(rangeDTO);
        rangeContoller.setRangeAreaController(this);
        rangeContoller.setIndex(countRanges++);
        ranges.put(rangeId, rangeContoller);
        rangeArea.getChildren().add(range);
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public void initializeRangeArea() {
        shitsellController.intitializeRangeArea(this);
    }

    public FlowPane getRangeArea() {
        return rangeArea;
    }

    public VBox getRangeAreaController() {
        return rangeAreaController;
    }

    public Button getCancel() {
        return cancel;
    }

    public void rangeClicked(RangeDTO rangeDTO, Button range) {
        shitsellController.rangeClicked(rangeDTO, range, rangeArea);
    }

    public void okClicked(String rangeName, String theRange) throws IOException {
        shitsellController.setRange(rangeName, theRange);
    }

    public Button getAddRange() {
        return addRange;
    }

    public Button getDeleteRange() {
        return deleteRange;
    }

    public void deleteSelcted(RangeController rangeController) {
        selcetedDelete.add(rangeController);
    }

    public void deleteUnSelcted(RangeController rangeController) {
        selcetedDelete.remove(rangeController);
    }

    public Button getSumbitDelete() {
        return sumbitDelete;
    }

    public void rest() {
        countRanges = 0;
        ranges.clear();
        rangeArea.getChildren().clear();
    }

    public List<String> getRanges() {
        return ranges.keySet().stream().toList();
    }
}


