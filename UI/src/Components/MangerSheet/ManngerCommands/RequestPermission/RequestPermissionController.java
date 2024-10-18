package Components.MangerSheet.ManngerCommands.RequestPermission;

import Components.Error.ErrorController;
import Components.MangerSheet.ManngerCommands.ManggerComandsController;
import dto.impl.PermissionType;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;

public class RequestPermissionController {

    @FXML
    private Button ok;

    @FXML
    private ChoiceBox<PermissionType> permission;

    @FXML
    private Label sheetName;

    private ManggerComandsController manggerComandsController;

    private SheetBasicData selctedSheet;

    @FXML
    void okClicked(ActionEvent event) {
        String sheetName = selctedSheet.getSheetName();
        String permission = this.permission.getValue().name();
        String finalUrl = HttpUrl
                .parse(Constants.PERMISSON_REQUEST)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .addQueryParameter("permission", permission)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.code() != 200){
                    Platform.runLater(() -> {
                        try {
                            ErrorController.showError("Error: " + response.body().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else{
                    Platform.runLater(() -> {
                        Stage stage = (Stage) ok.getScene().getWindow();
                        stage.close();
                    });
                    response.body().string();
                }

            }
        });
    }

    public void initialize() {
        permission.getItems().add(PermissionType.READER);
        permission.getItems().add(PermissionType.WRITER);
        ok.disableProperty().bind(permission.valueProperty().isNull());
    }

    public void init(){
        selctedSheet = manggerComandsController.getSelctedSheet();
        sheetName.setText("Request for permission to access the " + selctedSheet.getSheetName() + " sheet");
    }

    public void setManggerComandsController(ManggerComandsController manggerComandsController) {
        this.manggerComandsController = manggerComandsController;
    }

}
