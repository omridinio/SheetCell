package Components.MangerSheet;


import Components.Error.ErrorController;
import Components.Main.MainController;
import Components.MangerSheet.AvailableSheets.AvailableSheetsController;
import Components.MangerSheet.ManngerCommands.ManggerComandsController;
import Components.MangerSheet.PermissionsTable.PermissionsTableController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;


public class ManggerSheetController {

    @FXML
    private TableView availableSheet;

    @FXML private AvailableSheetsController availableSheetsController;

    @FXML
    private TableView permission;

    @FXML private PermissionsTableController permissionsTableController;

    @FXML
    private VBox commands;

    @FXML private ManggerComandsController manggerComandsController;

    @FXML
    private Button loadSheet;

    private MainController mainController;

    @FXML
    void loadSheetClicked(ActionEvent event){
        try{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            if(file == null) {
                return;
            }
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(),
                            RequestBody.create(MediaType.parse("application/octet-stream"),
                                    new File(file.getAbsolutePath())))
                    .build();
            Request request = new Request.Builder()
                    .url("http://localhost:8080/web/loadSheet")
                    .method("POST", body)
                    .build();

            HttpClientUtil.runAsync(request, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Platform.runLater(() -> {
                        ErrorController.showError(e.getMessage());
                    });
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        System.out.println("Sheet loaded");
                    } else {
                        Platform.runLater(() -> {
                            try {
                                ErrorController.showError(response.body().string());
                            } catch (IOException e) {

                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            String errorMessage = "Error: the load filed because: " + e.getMessage();
            ErrorController.showError(errorMessage);
        }
    }

    public void initialize() {
        if(availableSheetsController != null) {
            availableSheetsController.setManggerSheetController(this);
        }
        if(permissionsTableController != null) {
            permissionsTableController.setManggerSheetController(this);
        }
        if(manggerComandsController != null) {
            manggerComandsController.setManggerSheetController(this);
        }

    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
