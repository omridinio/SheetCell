package Components.Main;

import Components.MangerSheet.ManggerSheetController;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Map;

public class MainController {

    @FXML
    private ScrollPane main;

    private AnchorPane mangger;

    private String userName;

    private ManggerSheetController manggerSheetController;

    public void initialize() {

    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

    public void changeLogin(Parent pane) {
        double width = pane.prefWidth(1);
        double height = pane.prefHeight(1);
        main.setPrefWidth(width + 10);
        main.setPrefHeight(height + 10);
        AnchorPane.setTopAnchor(pane, 1.0);
        AnchorPane.setBottomAnchor(pane, 1.0);
        AnchorPane.setLeftAnchor(pane, 1.0);
        AnchorPane.setRightAnchor(pane, 1.0);
        main.setContent(pane);
        main.layout();
        Stage stage = (Stage) main.getScene().getWindow(); // Get the current stage
        stage.setWidth(width + 35);  // Adding some padding or margin
        stage.setHeight(height + 55); // Adding some padding or margin
    }

    public void LoadManger(AnchorPane mangger) {
        double width = mangger.prefWidth(1);
        double height = mangger.prefHeight(1);
        main.setPrefWidth(width + 10);
        main.setPrefHeight(height + 10);
        AnchorPane.setTopAnchor(mangger, 1.0);
        AnchorPane.setBottomAnchor(mangger, 1.0);
        AnchorPane.setLeftAnchor(mangger, 1.0);
        AnchorPane.setRightAnchor(mangger, 1.0);
        main.setContent(mangger);
        main.layout();
        this.mangger = mangger;
        mangger.setDisable(false);
        Stage stage = (Stage) main.getScene().getWindow(); // Get the current stage
        stage.setWidth(width + 35);  // Adding some padding or margin
        stage.setHeight(height + 55); // Adding some padding or margin
        main.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() > width + 10) {
                mangger.setPrefWidth(newValue.doubleValue() - 5);
            }
        });
        main.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() > height + 10) {
                mangger.setPrefHeight(newValue.doubleValue() - 5);
            }
        });
    }

    public void switchManger() {
        mangger.setDisable(false);
        main.setContent(mangger);
    }

    public void switchSheet(AnchorPane sheet) {
        double width = sheet.prefWidth(1);
        double height = sheet.prefHeight(1);
        sheet.setPrefHeight(mangger.getPrefHeight());
        sheet.setPrefWidth(mangger.getPrefWidth());
        main.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() > width + 10) {
                sheet.setPrefWidth(newValue.doubleValue() - 5);
            }
        });
        main.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.doubleValue() > height + 10) {
                sheet.setPrefHeight(newValue.doubleValue() - 5);
            }
        });
        sheet.requestLayout();
        main.setContent(sheet);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void close() {
        manggerSheetController.close();
    }

    public void setDisable() {
        mangger.setDisable(true);
    }

    public void setEnable() {
        mangger.setDisable(false);
    }
}
