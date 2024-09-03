package Components.Range.setRange;

import Components.Shitcell.ShitsellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class setRangeController {

    @FXML
    private TextField rangeName;

    @FXML
    private TextField theRange;

    @FXML
    private Button okButtom;

    private ShitsellController shitsellController;

    public void initialize() {
        okButtom.disableProperty().bind(rangeName.textProperty().isEmpty().or(theRange.textProperty().isEmpty()));
    }

    public void setShitsellController(ShitsellController shitsellController) {
        this.shitsellController = shitsellController;
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) okButtom.getScene().getWindow();
        stage.close();
    }

    @FXML
    void okClicked(ActionEvent event) {
        if(isRangeValid()){
            try {
                shitsellController.setRange(rangeName.getText(), theRange.getText());
                Stage stage = (Stage) okButtom.getScene().getWindow();
                stage.close();
            } catch (IllegalArgumentException e) {

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isRangeValid(){
        String regex = "^[A-Z]\\d+\\.\\.[A-Z]\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(theRange.getText());
        return matcher.matches();
    }

}
