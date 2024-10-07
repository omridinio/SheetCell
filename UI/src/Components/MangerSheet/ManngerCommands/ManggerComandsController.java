package Components.MangerSheet.ManngerCommands;

import Components.MangerSheet.ManggerSheetController;
import Components.MangerSheet.ManngerCommands.AckOrDnyPer.AckOrDnyPerController;
import Components.MangerSheet.ManngerCommands.RequestPermission.RequestPermissionController;
import dto.impl.PermissionRequest;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.List;

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
        String finalUrl = HttpUrl
                .parse(Constants.PERMISSION_OWNER)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {

                }
                else {
                    String jsonArrayOfSheetNames = response.body().string();
                    PermissionRequest[] permissionRequests = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, PermissionRequest[].class);
                    List<PermissionRequest> permissionRequestList = List.of(permissionRequests);
                    createPermissionRequests(permissionRequestList);
                }
            }
        });
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

    private void createPermissionRequests(List<PermissionRequest> permissionRequestList) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/ManngerCommands/AckOrDnyPer/ackOrDnyPer.fxml"));
        Parent newWindowRoot = loader.load();
        AckOrDnyPerController ackOrDnyPerController = loader.getController();
        ackOrDnyPerController.insertPermissions(permissionRequestList);
        Platform.runLater(() -> {
            Popup popup = new Popup();
            popup.setAutoHide(true); // Automatically hide when clicking outside
            popup.getContent().clear();
            popup.getContent().add(newWindowRoot);
            popup.show(ackOrDeny, ackOrDeny.localToScreen(0, ackOrDeny.getHeight()).getX(), ackOrDeny.localToScreen(0, ackOrDeny.getHeight()).getY());
        });
    }
}
