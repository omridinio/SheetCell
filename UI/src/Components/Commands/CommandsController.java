package Components.Commands;

import Components.Commands.SetCommand.SetCommandController;
import Components.Range.setRange.setRangeController;
import Components.Shitcell.ShitsellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CommandsController {

    @FXML
    private Button sort;

    @FXML
    private AnchorPane commandArea;

    private ShitsellController shitsellController;

    @FXML
    void clickeSort(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/SetCommand/SetCommand.fxml"));
        Parent newWindowRoot = loader.load();
        SetCommandController setCommandController = loader.getController();
        setCommandController.setCommandsController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Sort range of the sheet");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public List<Integer> Vclicked(String range) throws IOException, ClassNotFoundException {
        return shitsellController.getColsInRange(range);
    }


    public void initializeCommands(){
        shitsellController.initializeCommands(this);
    }


    public void okClicked(String theRange, List<Integer> cols) throws IOException, ClassNotFoundException {
        shitsellController.sortRangeClicked(theRange, cols);
    }

    public Button getSort() {
        return sort;
    }

    public AnchorPane getCommandArea() {
        return commandArea;
    }
}