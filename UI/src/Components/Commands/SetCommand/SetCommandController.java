package Components.Commands.SetCommand;

import Components.Commands.CheckRange.CheckRangeController;
import Components.Commands.Commands;
import Components.Commands.CommandsController;
import Components.Error.ErrorController;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SetCommandController implements Commands {

    private CommandsController commandsController;

    @FXML
    private Button OK;

    @FXML
    private Button addLevelButtom;

    @FXML
    private AnchorPane checkRange;

    @FXML
    private CheckRangeController checkRangeController;

    @FXML
    private FlowPane colsChoose;

    @FXML
    private Button deleteLevel;


    public void initialize() {
        OK.setDisable(true);
        addLevelButtom.setDisable(true);
        deleteLevel.setDisable(true);
        if(checkRangeController != null){
            checkRangeController.setCommands(this);
        }
    }


    @Override
    public boolean VClicked() throws IOException, ClassNotFoundException {
        if(checkRangeController.isRangeValid()) {
            try {
                List<Integer> colsRange = commandsController.Vclicked(checkRangeController.getTheRange().getText().toUpperCase());
                ChoiceBox<Character> firstChoiceBox = checkRangeController.createFirstChoiceBox(colsRange);
                if (!firstChoiceBox.getItems().isEmpty()) {
                    colsChoose.getChildren().add(firstChoiceBox);
                    checkRangeController.initPart2(this.OK, this.addLevelButtom);
                }
                return true;
            } catch (Exception e) {
                ErrorController.showError(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void XClicked() {
        colsChoose.getChildren().clear();
        checkRangeController.getVButton().setDisable(false);
        deleteLevel.setDisable(true);
        checkRangeController.getXButton().setDisable(true);
    }


    @FXML
    void addLevelClicked(ActionEvent event) {
        ChoiceBox<Character> newChoiceBox = checkRangeController.addLevelClicked();
        colsChoose.getChildren().add(newChoiceBox);
        deleteLevel.setDisable(false);
    }

    @FXML
    void deleteLevelClicked(ActionEvent event) {
        colsChoose.getChildren().remove(colsChoose.getChildren().size() - 1);
        if (checkRangeController.deleteLevelClicked())
            deleteLevel.setDisable(true);
    }

    @FXML
    void okClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        List<Integer> dominantCols = checkRangeController.createDominantCols();
        try {
            commandsController.okClicked(checkRangeController.getTheRange().getText().toUpperCase(), dominantCols);
            Stage stage = (Stage) OK.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }


}
