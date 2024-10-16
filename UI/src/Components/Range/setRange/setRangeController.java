package Components.Range.setRange;

import Components.Error.ErrorController;
import Components.RangeArea.RangeAreaController;
import Components.Shitcell.ShitsellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class setRangeController {

    @FXML
    private TextField rangeName;

    @FXML
    private TextField theRange;

    @FXML
    private Button okButtom;

    @FXML
    private VBox errorMessege;

    private RangeAreaController rangeAreaController;

    public void initialize() {
        okButtom.disableProperty().bind(rangeName.textProperty().isEmpty().or(theRange.textProperty().isEmpty()));
    }

    public void setRangeAreaController(RangeAreaController rangeAreaController) {
        this.rangeAreaController = rangeAreaController;
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) okButtom.getScene().getWindow();
        stage.close();
    }

    @FXML
    void okClicked(ActionEvent event) throws IOException {
        if(isRangeValid()){
            try {
                errorMessege.visibleProperty().setValue(false);
                rangeAreaController.okClicked(rangeName.getText(), theRange.getText().toUpperCase());
                Stage stage = (Stage) okButtom.getScene().getWindow();
                stage.close();
            } catch (IllegalArgumentException e) {
                ErrorController.showError(e.getMessage());
            }
            catch (Exception e) {
                ErrorController.showError(e.getMessage());
            }
        }
        else
            errorMessege.visibleProperty().setValue(true);
    }

    private boolean isRangeValid(){
        String regex = "^[A-Z]\\d+\\.\\.[A-Z]\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(theRange.getText().toUpperCase());
        return matcher.matches();
    }

}
