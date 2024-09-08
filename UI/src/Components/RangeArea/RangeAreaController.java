package Components.RangeArea;

import Components.Range.RangeController;
import Components.Range.setRange.setRangeController;
import Components.Shitcell.ShitsellController;
import body.impl.CoordinateImpl;
import dto.impl.CellDTO;
import dto.impl.RangeDTO;
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
import java.util.List;

public class RangeAreaController {

    @FXML
    private FlowPane rangeArea;

    @FXML
    private VBox rangeAreaController;

    private ShitsellController shitsellController;

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

    public void initialize() {}

    public void addRange(String rangeId, RangeDTO rangeDTO) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/Range.fxml"));
        Node range = loader.load();
        RangeController rangeContoller = loader.getController();
        rangeContoller.setRangeDTO(rangeDTO);
        rangeContoller.setRangeAreaController(this);
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

    public void rangeClicked(RangeDTO rangeDTO, Button range) {
        shitsellController.rangeClicked(rangeDTO, range, rangeArea);
    }

    public void okClicked(String rangeName, String theRange) throws IOException {
        shitsellController.setRange(rangeName, theRange);
    }
}
