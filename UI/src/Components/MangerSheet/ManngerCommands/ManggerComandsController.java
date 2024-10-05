package Components.MangerSheet.ManngerCommands;

import Components.MangerSheet.ManggerSheetController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ManggerComandsController {

    @FXML
    private Button ackOrDeny;

    @FXML
    private Button request;

    @FXML
    private Button viewSheet;

    private ManggerSheetController manggerSheetController;

    @FXML
    void ackOrDentClicked(ActionEvent event) {

    }

    @FXML
    void requestClicked(ActionEvent event) {

    }

    @FXML
    void viewSheetClicked(ActionEvent event) {

    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

}
