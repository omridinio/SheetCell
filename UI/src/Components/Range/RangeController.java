package Components.Range;

import Components.RangeArea.RangeAreaController;
import dto.impl.RangeDTO;
import expression.Range;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

    private BooleanProperty deleteMode = new SimpleBooleanProperty(false);

    public void initialize() {
        delete.disableProperty().bind(deleteMode.not());
        labelCheckBox.visibleProperty().bind(deleteMode);
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
    }

    public double getWidth() {
        return range.getWidth();
    }

    public double getHeight() {
        return range.getHeight();
    }

    public void clicked(ActionEvent event) {
       //shitsellController.rangeClicked(rangeDTO, range);
        rangeControllerArea.rangeClicked(rangeDTO, range);
    }






}