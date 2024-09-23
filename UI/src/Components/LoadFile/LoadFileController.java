package Components.LoadFile;

import Components.Shitcell.ShitsellController;
import Properties.TaskLoadFile;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class LoadFileController {

    @FXML
    private Label precent;

    @FXML
    private ProgressBar prosses;

    private ShitsellController shitsellController;

    public void bindProperty(TaskLoadFile taskLoadFile){
        prosses.progressProperty().bind(taskLoadFile.progressProperty());
        precent.textProperty().bind(taskLoadFile.progressProperty().multiply(100).asString("%.0f %%"));
    }

    public void setShitsellController(ShitsellController shitsellController) {
        this.shitsellController = shitsellController;
    }


}
