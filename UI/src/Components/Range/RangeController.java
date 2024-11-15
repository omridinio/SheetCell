package Components.Range;

import Components.RangeArea.RangeAreaController;
import dto.impl.RangeDTO;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import Components.Shitcell.ShitsellController;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;


public class RangeController {

    @FXML
    private Button range;
    private RangeAreaController rangeControllerArea;
    ShitsellController shitsellController;
    private RangeDTO rangeDTO;
    private int index;

    @FXML
    private CheckBox delete;

    @FXML
    private Label labelCheckBox;

    @FXML
    void rangeSelcted(ActionEvent event) {
        if(delete.isSelected()) {
            labelCheckBox.getStyleClass().add("selected");
            rangeControllerArea.deleteSelcted(this);
        }
        else {
            labelCheckBox.getStyleClass().remove("selected");
            rangeControllerArea.deleteUnSelcted(this);
        }
    }

    public void unSelect() {
        delete.setSelected(false);
        labelCheckBox.getStyleClass().remove("selected");
    }

    private BooleanProperty deleteMode = new SimpleBooleanProperty(false);

    public void initialize() {
        delete.disableProperty().bind(deleteMode.not());
        labelCheckBox.visibleProperty().bind(deleteMode);
        range.setAlignment(Pos.CENTER_LEFT);
    }

    public String getRangeId() {
        return rangeDTO.getRangeId();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void deleteModeOn() {
        delete.setSelected(false);
        deleteMode.setValue(true);
    }

    public void deleteModeOff() {
        deleteMode.setValue(false);
        delete.setSelected(false);
        labelCheckBox.getStyleClass().remove("selected");
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public void setRangeAreaController(RangeAreaController rangeAreaController){
        this.rangeControllerArea = rangeAreaController;
    }

    public void setRangeDTO(RangeDTO rangeDTO){
        this.rangeDTO = rangeDTO;
        setText(rangeDTO.getRangeId());
    }

    public Button getRange() {
        return range;
    }

    public void setText(String text) {
        range.setText(text);
        range.setAlignment(Pos.CENTER_LEFT);
    }

    public double getWidth() {
        return range.getWidth();
    }

    public double getHeight() {
        return range.getHeight();
    }

    public void clicked(ActionEvent event) {
        rangeControllerArea.rangeClicked(rangeDTO, range);
    }






}