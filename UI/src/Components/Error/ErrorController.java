package Components.Error;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorController {

    @FXML
    private Label errorMessege;

    @FXML
    void okClicked(ActionEvent event) {
        Stage stage = (Stage) errorMessege.getScene().getWindow();
        stage.close();
    }

    public void setErrorMessege(String errorMessege) {
        this.errorMessege.setText(errorMessege);
    }

    public static void showError(String errorMessege) {
            Platform.runLater(() -> {
                try {
                FXMLLoader loader = new FXMLLoader(ErrorController.class.getResource("/Components/Error/error.fxml"));
                Parent newWindows = loader.load();
                ErrorController errorController = loader.getController();
                errorController.setErrorMessege(errorMessege);
                Stage stage = new Stage();
                stage.setTitle("Error");
                stage.setScene(new Scene(newWindows));
                stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

    }

}
