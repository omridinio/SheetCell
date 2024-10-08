package Components.Login;

import Components.Error.ErrorController;
import Components.Main.MainController;
import Components.MangerSheet.ManggerSheetController;
import jakarta.xml.bind.SchemaOutputResolver;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import okhttp3.*;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.net.http.HttpClient;

public class LoginController {


    @FXML
    private Label errorMessge;

    @FXML
    private Button login;

    @FXML
    private TextField theUserName;

    private MainController mainController;

    @FXML
    void loginClicked(ActionEvent event) throws IOException {
        try {
            String userName = theUserName.getText();
            String finalUrl = HttpUrl
                    .parse(Constants.LOGIN_PAGE)
                    .newBuilder()
                    .addQueryParameter("username", userName)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException, RuntimeException {
                    if (response.code() == 200) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/mangerSheet.fxml"));
                        Parent mangerSheet = loader.load();
                        ManggerSheetController manggerSheetController = loader.getController();
                        manggerSheetController.setMainController(mainController);
                        manggerSheetController.setUserName(userName);
                        Platform.runLater(() -> {
                            mainController.setPane(mangerSheet);
                        });
                    } else {
                        Platform.runLater(() -> {
                            errorMessge.setVisible(true);
                            try {
                                errorMessge.setText("Error: " + response.body().string());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                    response.body().string();
                }
            });
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }

    }

    public void initialize() {
        login.disableProperty().bind(theUserName.textProperty().isEmpty());
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


}
