package Components.Cell;

import Components.Shitcell.ShitsellController;
import body.Coordinate;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.List;

public class CellContoller {

    @FXML
    private Button cell;
    private ShitsellController shitsellController;
    private CellDTO cellDTO;


    public void initialize() {

    }

    @FXML
    void clicked(ActionEvent event) {
        shitsellController.cellClicked(cellDTO, cell);
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public void setCellDTO(CellDTO cellDTO){
        this.cellDTO = cellDTO;
        setText(cellDTO.getOriginalEffectiveValue().toString());
    }

    public Button getCell() {
        return cell;
    }

    public String getText() {
        return cell.getText();
    }

    public void setText(String text) {
        cell.setText(text);
    }

    public double getWidth() {
        return cell.getWidth();
    }

    public double getHeight() {
        return cell.getHeight();
    }

    public void setWidth(double width) {
        cell.setPrefWidth(width);
    }

    public void setHeight(double height) {
        cell.setPrefHeight(height);
    }
}
