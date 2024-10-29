package Components.MangerSheet.ManngerCommands;

import Components.Error.ErrorController;
import Components.MangerSheet.ManggerSheetController;
import Components.MangerSheet.ManngerCommands.AckOrDnyPer.AckOrDnyPerController;
import Components.MangerSheet.ManngerCommands.RequestPermission.RequestPermissionController;
import Components.Shitcell.ShitsellController;
import dto.impl.CoordinateAdapter;
import dto.impl.Coordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.SheetDTO;
import dto.impl.ImplSheetDTO;
import dto.impl.PermissionRequest;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
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

    private Popup requestPopup = new Popup();

    private Popup ackOrDnyPopup = new Popup();


    @FXML
    void ackOrDentClicked(ActionEvent event) {
        if (ackOrDnyPopup.isShowing()) {
            ackOrDnyPopup.hide();
            return;
        }
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
        if(requestPopup.isShowing()){
            requestPopup.hide();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/ManngerCommands/RequestPermission/requestPermission.fxml"));
        Parent newWindowRoot = loader.load();
        RequestPermissionController requestPermissionController = loader.getController();
        requestPermissionController.setManggerComandsController(this);
        requestPermissionController.init();
        requestPopup.setAutoHide(true); // Automatically hide when clicking outside
        requestPopup.getContent().clear();
        requestPopup.getContent().add(newWindowRoot);
        requestPopup.show(request, request.localToScreen(-120, request.getHeight()).getX(), request.localToScreen(0, request.getHeight()).getY());
    }

    @FXML
    void viewSheetClicked(ActionEvent event) {
        try {
            manggerSheetController.setDisable();
            String finalUrl = HttpUrl
                    .parse(Constants.VIEW_SHEET)
                    .newBuilder()
                    .addQueryParameter("sheetName", manggerSheetController.getSheetNameProperty().get())
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        ErrorController.showError(e.getMessage());
                        manggerSheetController.setEnable();
                    });
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        Platform.runLater(() -> {
                            try {
                                ErrorController.showError(response.body().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            manggerSheetController.setEnable();
                        });
                    }
                    else {
                        String jsonResponse = response.body().string();
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                                .create();
                        SheetDTO sheetDTO = gson.fromJson(jsonResponse, ImplSheetDTO.class);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Shitcell/Shitsel.fxml"));
                        AnchorPane root = loader.load();
                        ShitsellController shitsellController = loader.getController();
                        shitsellController.setManggerSheetController(manggerSheetController);
                        manggerSheetController.setCurrSheetController(shitsellController);
                        manggerSheetController.setIsSheetSelected(false);
                        manggerSheetController.setInScreen(false);
                        Platform.runLater(() -> {
                            manggerSheetController.changeContent(root);
                            shitsellController.showSheet(sheetDTO, manggerSheetController.getSelectedSheet().getSheetPermission());
                            manggerSheetController.clearSheetSelect();
                        });
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
            ErrorController.showError(e.getMessage());
        }
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

        viewSheet.disableProperty().bind(manggerSheetController.isSheetSelectedProperty().and(manggerSheetController.getHavePermission()).not());

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
            ackOrDnyPopup.setAutoHide(true); // Automatically hide when clicking outside
            ackOrDnyPopup.getContent().clear();
            ackOrDnyPopup.getContent().add(newWindowRoot);
            ackOrDnyPopup.show(ackOrDeny, ackOrDeny.localToScreen(-100, ackOrDeny.getHeight()).getX(), ackOrDeny.localToScreen(0, ackOrDeny.getHeight()).getY());
        });
    }

    public void hideRequestPermission() {
        requestPopup.hide();
    }
}
