package Components.Range;

import dto.impl.RangeDTO;
import expression.Range;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import Components.Shitcell.ShitsellController;

public class RangeController {

    @FXML
    private Button range;
    ShitsellController shitsellController;
    private RangeDTO rangeDTO;

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
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
       shitsellController.rangeClicked(rangeDTO, range);
    }




}