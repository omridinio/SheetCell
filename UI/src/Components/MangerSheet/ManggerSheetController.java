package Components.MangerSheet;


import Components.Error.ErrorController;
import Components.Main.MainController;
import Components.MangerSheet.AvailableSheets.AvailableSheetsController;
import Components.MangerSheet.ManngerCommands.ManggerComandsController;
import Components.MangerSheet.PermissionsTable.PermissionsTableController;
import dto.impl.PermissionType;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;


public class ManggerSheetController {

    @FXML
    private TableView availableSheets;

    @FXML private AvailableSheetsController availableSheetsController;

    @FXML
    private TableView permissionsTable;

    @FXML private PermissionsTableController permissionsTableController;

    @FXML
    private VBox manggerComands;

    @FXML private ManggerComandsController manggerComandsController;

    @FXML
    private Button loadSheet;

    private SimpleStringProperty userName = new SimpleStringProperty("");

    private SimpleStringProperty OwnerSelcetedSheet = new SimpleStringProperty("");

    private MainController mainController;

    private SheetBasicData selectedSheet;

    private SimpleBooleanProperty inScreen;

    private SimpleBooleanProperty isSheetSelected = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty havePermission = new SimpleBooleanProperty(false);

    private SimpleStringProperty sheetName = new SimpleStringProperty();

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
                    .url(Constants.LOAD_SHEET)
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
                                String messege = response.body().string();
                                ErrorController.showError(messege);
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
        inScreen = new SimpleBooleanProperty(true);
        isSheetSelected = new SimpleBooleanProperty(false);
        if(availableSheetsController != null) {
            availableSheetsController.setManggerSheetController(this);
            availableSheetsController.init();
        }
        if(permissionsTableController != null) {
            permissionsTableController.setManggerSheetController(this);
            permissionsTableController.init();
        }
        if(manggerComandsController != null) {
            manggerComandsController.setManggerSheetController(this);
            manggerComandsController.init();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setSelectedSheet(SheetBasicData selectedSheet) {
        this.selectedSheet = selectedSheet;
        isSheetSelected.set(true);
        sheetName.set(selectedSheet.getSheetName());
        OwnerSelcetedSheet.set(selectedSheet.getSheetOwner());
        if(selectedSheet.getSheetPermission() != PermissionType.NONE || selectedSheet.getSheetOwner().equals(userName)){
            havePermission.setValue(true);
        }
        else {
            havePermission.setValue(false);
        }

    }

    public SheetBasicData getSelectedSheet() {
        return selectedSheet;
    }

    public BooleanProperty inScreenProperty() {
        return inScreen;
    }

    public BooleanProperty isSheetSelectedProperty() {
        return isSheetSelected;
    }

    public StringProperty getSheetNameProperty() {
        return sheetName;
    }

    public void setIsSheetSelected(boolean value) {
        isSheetSelected.setValue(value);
    }

    public void setInScreen(boolean value) {
        inScreen.setValue(value);
    }

    public void setUserName(String userName) {
        this.userName.set(userName);
    }

    public SimpleStringProperty getUserName() {
        return userName;
    }

    public SimpleStringProperty getOwnerSelcetedSheet() {
        return OwnerSelcetedSheet;
    }

    public SimpleBooleanProperty getHavePermission(){
        return havePermission;
    }

    public void changeContent(Parent pane){
            mainController.setPane(pane);
    }



}
