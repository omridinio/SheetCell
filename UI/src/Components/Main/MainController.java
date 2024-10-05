package Components.Main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utils.HttpClientUtil;

public class MainController {

    @FXML
    private ScrollPane main;


    public void initialize() {

    }

    public void setPane(Parent pane) {
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
        stage.setWidth(width + 100);  // Adding some padding or margin
        stage.setHeight(height + 100); // Adding some padding or margin
    }
}
