package Components.MangerSheet;


import Components.Chat.ChatAreaController;
import Components.Error.ErrorController;
import Components.Main.MainController;
import Components.MangerSheet.AvailableSheets.AvailableSheetsController;
import Components.MangerSheet.ManngerCommands.ManggerComandsController;
import Components.MangerSheet.PermissionsTable.PermissionsTableController;
import Components.Shitcell.ShitsellController;
import dto.impl.PermissionType;
import dto.impl.SheetBasicData;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TableView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Window;
import javafx.util.Duration;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;


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

    @FXML
    private Label hello;

    @FXML
    private Button chat;

    @FXML
    private Label sheetLoad;

    private ShitsellController currentSheetController = null;

    private SimpleStringProperty userName = new SimpleStringProperty("");

    private SimpleStringProperty OwnerSelcetedSheet = new SimpleStringProperty("");

    private MainController mainController;

    private SheetBasicData selectedSheet;

    private SimpleBooleanProperty inScreen;

    private SimpleBooleanProperty isSheetSelected = new SimpleBooleanProperty(false);

    private SimpleBooleanProperty havePermission = new SimpleBooleanProperty(false);

    private SimpleStringProperty sheetName = new SimpleStringProperty();

    private ScrollPane chatArea;

    private ChatAreaController chatAreaController;


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
                        try {
                            loadSucsses(response.body().string());
                        } catch (InterruptedException e) {
                            ErrorController.showError(e.getMessage());
                        }
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

    @FXML
    void chatClicked(ActionEvent event) {
        chatClick(this.chat);
    }

    private void loadSucsses(String messege) throws InterruptedException {
        Platform.runLater(() -> {
            sheetLoad.setText(messege);
        });
        sleep(3000);
        Platform.runLater(() -> {
            sheetLoad.setText("");
        });
    }

    public void chatClick(Button chat) {
        Popup popup = new Popup();
        popup.setAutoHide(true); // Automatically hide when clicking outside
        popup.getContent().clear();
        popup.getContent().add(chatArea);

        // Get the window where the chat button resides (the main window)
        Window window = chat.getScene().getWindow();

        // Get the position of the button in screen coordinates
        double buttonX = chat.localToScreen(0, 0).getX();
        double buttonY = chat.localToScreen(0, 0).getY();

        // Define the height of the chat area
        double chatAreaHeight = 438; // Set this to the known height of chatArea
        double popupOffsetX = -100; // Move popup left
        double popupOffsetY = 0; // Move popup up

        // Show the popup at the new position
        popup.show(chat, buttonX + popupOffsetX, buttonY - chatAreaHeight + popupOffsetY);
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
        initChatArea();
    }

    private void initChatArea() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Chat/chat.fxml"));
        try {
            chatArea = loader.load();
            chatAreaController = loader.getController();
            chatAreaController.setManggerSheetController(this);
            chatAreaController.init();
        } catch (IOException e) {
            ErrorController.showError(e.getMessage());
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
        hello.setText("Hello " + userName);
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

    public void changeContent(ScrollPane pane){
            //mainController.setPane(pane);
        mainController.switchSheet(pane);
    }

    public void switchManagerSheet(){
        setInScreen(true);
        currentSheetController = null;
        mainController.switchManger();
    }

    public void clearSheetSelect() {
        availableSheetsController.clearSheetSelct();
        selectedSheet = null;
    }

    public void setCurrSheetController(ShitsellController currSheet) {
        currentSheetController = currSheet;
    }


    public void close() {
        availableSheetsController.close();
        permissionsTableController.close();
        if(currentSheetController != null) {
            currentSheetController.close();
        }
        chatAreaController.close();
    }

    public void setDisable() {
        mainController.setDisable();
    }

    public void setEnable() {
        mainController.setEnable();
    }

    public ObservableValue<String> getHelloProperty() {
        return hello.textProperty();
    }
}
