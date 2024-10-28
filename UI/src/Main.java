
import Components.Login.LoginController;
import Components.Main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import utils.HttpClientUtil;


public class Main extends Application {
    private MainController mainController;


    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Shitcell");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Main/main.fxml"));
        ScrollPane root = loader.load();
        mainController = loader.getController();
        FXMLLoader loaderLogin = new FXMLLoader(getClass().getResource("/Components/Login/login.fxml"));
        AnchorPane login = loaderLogin.load();
        LoginController loginController = loaderLogin.getController();
        loginController.setMainController(mainController);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        mainController.changeLogin(login);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        HttpClientUtil.shutdown();
        mainController.close();
    }
}
