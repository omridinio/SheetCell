package Components.MangerSheet.ManngerCommands;

import Components.MangerSheet.ManggerSheetController;
import Components.MangerSheet.ManngerCommands.RequestPermission.RequestPermissionController;
import dto.impl.SheetBasicData;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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
    void requestClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/ManngerCommands/RequestPermission/requestPermission.fxml"));
        Parent newWindowRoot = loader.load();
        RequestPermissionController requestPermissionController = loader.getController();
        requestPermissionController.setManggerComandsController(this);
        requestPermissionController.init();
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Permission Request");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    @FXML
    void viewSheetClicked(ActionEvent event) {

    }

    public void initialize() {

    }

    public void init() {
        request.disableProperty().bind(Bindings.createBooleanBinding(
                () -> {
                    boolean isOwner = manggerSheetController.getUserName().get().equals(manggerSheetController.getOwnerSelcetedSheet().get());
                    boolean isSheetSelected = manggerSheetController.isSheetSelectedProperty().get();
                    return isOwner || !isSheetSelected;
                },
                manggerSheetController.getUserName(),
                manggerSheetController.getOwnerSelcetedSheet(),
                manggerSheetController.isSheetSelectedProperty()
        ));
    }


    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

    public SheetBasicData getSelctedSheet() {
        return manggerSheetController.getSelectedSheet();
    }
}
