package Components.RangeArea;

import Components.Error.ErrorController;
import Components.Range.RangeController;
import Components.Range.setRange.setRangeController;
import Components.Shitcell.ShitsellController;
import dto.impl.RangeDTO;
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
import java.util.*;

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

    private RangeController rangeToDelete = null;

    private int countRanges = 0;

    private SimpleBooleanProperty deleteSelcet = new SimpleBooleanProperty(false);


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
            shitsellController.deleteRange(rangeToDelete.getRangeId());
//            rangeArea.getChildren().remove(rangeToDelete.getIndex());
//            ranges.remove(rangeToDelete.getRangeId());
//            for (RangeController range : ranges.values()) {
//                if(range.getIndex() > rangeToDelete.getIndex()) {
//                    range.setIndex(range.getIndex() - 1);
//                }
//            }
//            rangeToDelete = null;
            for (RangeController range : ranges.values()) {
                range.deleteModeOff();
            }
            selcetedDelete.clear();
            shitsellController.deleteRangeModeOff();
            deleteSelcet.setValue(false);
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        shitsellController.deleteRangeModeOff();
        for (RangeController range : ranges.values()) {
            range.deleteModeOff();
        }
        rangeToDelete = null;
        deleteSelcet.setValue(false);
        selcetedDelete.clear();
    }

    public void initialize() {
        sumbitDelete.disableProperty().bind(deleteSelcet.not());
    }

    public void addRange(String rangeId, RangeDTO rangeDTO) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/range.fxml"));
        Node range = loader.load();
        RangeController rangeContoller = loader.getController();
        rangeContoller.setRangeDTO(rangeDTO);
        rangeContoller.setRangeAreaController(this);
        rangeContoller.setIndex(countRanges++);
        ranges.put(rangeId, rangeContoller);
        rangeArea.getChildren().add(range);
    }

    public void restRangeArea() {
        rangeArea.getChildren().clear();
        ranges.clear();
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
        if(rangeToDelete != null) {
            rangeToDelete.unSelect();
        }
        rangeToDelete = rangeController;
        deleteSelcet.setValue(true);
    }

    public void deleteUnSelcted(RangeController rangeController) {
        rangeToDelete = null;
        deleteSelcet.setValue(false);
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

    public void clearRanges(){

    }
}


