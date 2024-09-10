package Components.Commands.SetCommand;

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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetCommandController {

    private CommandsController commandsController;

    @FXML
    private Button OK;

    @FXML
    private Button addLevelButtom;

    @FXML
    private FlowPane colsChoose;

    @FXML
    private Button deleteLevel;

    @FXML
    private TextField theRange;

    @FXML
    private Button vButton;

    @FXML
    private Button xButton;

    @FXML
    private VBox errorMessege;

    private List<Character> colsInRange = new ArrayList<>();

    private ObservableList<ChoiceBox<Character>> choiceBoxes = FXCollections.observableArrayList();

    private BooleanBinding anyEmptyOrUnselectedBinding;



    public void initialize() {
        OK.setDisable(true);
        addLevelButtom.setDisable(true);
        deleteLevel.setDisable(true);
        xButton.setDisable(true);
    }

    @FXML
    void VClicked(ActionEvent event) throws IOException {
        if(isRangeValid()){
            try {
                errorMessege.setVisible(false);
                List<Integer> colsRange = commandsController.Vclicked(theRange.getText().toUpperCase());
                ChoiceBox<Character> firstChoiceBox = new ChoiceBox<Character>();
                colsChoose.getChildren().add(firstChoiceBox);
                for (int i = colsRange.get(0); i <= colsRange.get(1); i++){
                    char col = (char) (i - 1 + 'A');
                    firstChoiceBox.getItems().add(col);
                    colsInRange.add(col);
                }
                choiceBoxes.add(firstChoiceBox);
                initPart2();
                vButton.setDisable(true);
                xButton.setDisable(false);
            } catch (Exception e) {
                ErrorController.showError(e.getMessage());
                e.printStackTrace();
            }
        }
        else {
            errorMessege.setVisible(true);
        }
    }

    @FXML
    void XClicked(ActionEvent event) {
        colsInRange.clear();
        choiceBoxes.clear();
        colsChoose.getChildren().clear();
        vButton.setDisable(false);
        deleteLevel.setDisable(true);
        xButton.setDisable(true);
    }


    private void initPart2(){
//        anyEmptyOrUnselectedBinding = Bindings.createBooleanBinding(
//                () -> choiceBoxes.stream().anyMatch(cb -> cb.getSelectionModel().getSelectedItem() == null || choiceBoxes.isEmpty()),
//                choiceBoxes
//        );
//        OK.disableProperty().bind(anyEmptyOrUnselectedBinding);
//        addLevelButtom.disableProperty().bind(anyEmptyOrUnselectedBinding.or(Bindings.size(choiceBoxes).isEqualTo(colsInRange.size())));
//        choiceBoxes.forEach(cb -> cb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//            anyEmptyOrUnselectedBinding.invalidate();
//        }));
        // Binding to check if the list is empty
        BooleanBinding isEmptyBinding = Bindings.createBooleanBinding(
                () -> choiceBoxes.isEmpty(),
                choiceBoxes
        );

        // Binding to check if any ChoiceBox has no selected item
        BooleanBinding anyUnselectedBinding = Bindings.createBooleanBinding(
                () -> choiceBoxes.stream().anyMatch(cb -> cb.getSelectionModel().getSelectedItem() == null),
                choiceBoxes.stream()
                        .map(cb -> cb.getSelectionModel().selectedItemProperty())
                        .toArray(Observable[]::new)
        );

        // Combine both conditions to create the final binding
        anyEmptyOrUnselectedBinding = isEmptyBinding.or(anyUnselectedBinding);

        // Bind the OK button disable property to the combined binding
        OK.disableProperty().bind(anyEmptyOrUnselectedBinding);

        // Bind the addLevelButton disable property to the combined binding with additional condition
        addLevelButtom.disableProperty().bind(
                anyEmptyOrUnselectedBinding.or(Bindings.size(choiceBoxes).isEqualTo(colsInRange.size()))
        );

        // Add listeners to each ChoiceBox to invalidate the binding when the selection changes
        choiceBoxes.forEach(cb -> cb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            anyEmptyOrUnselectedBinding.invalidate();
        }));
    }

    @FXML
    void addLevelClicked(ActionEvent event) {
        ChoiceBox<Character> newChoiceBox = new ChoiceBox<Character>();
        colsChoose.getChildren().add(newChoiceBox);
        for (char col : colsInRange){
            newChoiceBox.getItems().add(col);
        }
        choiceBoxes.add(newChoiceBox);
        newChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            anyEmptyOrUnselectedBinding.invalidate();
        });
        deleteLevel.setDisable(false);
    }

    @FXML
    void deleteLevelClicked(ActionEvent event) {
        if (choiceBoxes.size() > 1){
            colsChoose.getChildren().remove(choiceBoxes.size() - 1);
            choiceBoxes.remove(choiceBoxes.size() - 1);
        }
        if (choiceBoxes.size() == 1){
            deleteLevel.setDisable(true);
        }
    }


    @FXML
    void okClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        List<Integer> dominantCols = createDominantCols();
        try {
            commandsController.okClicked(theRange.getText().toUpperCase(), dominantCols);
            Stage stage = (Stage) OK.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }

    }

    private List<Integer> createDominantCols(){
        List<Character> cols = new ArrayList<>();
        List<Integer> dominantCol = new ArrayList<>();
        for (ChoiceBox<Character> cb : choiceBoxes){
            //int col = (int) cb.getSelectionModel().getSelectedItem() - 'A' + 1;
            cols.add(cb.getSelectionModel().getSelectedItem());
        }
        int index = findIndex(cols.getLast());
        for (int i = index + 1; i < colsInRange.size(); i++){
            if(!cols.contains(colsInRange.get(i)))
                cols.add(colsInRange.get(i));
        }
        for (int i = 0; i < index; i++){
            if(!cols.contains(colsInRange.get(i)))
                cols.add(colsInRange.get(i));
        }
        for (char col : cols){
            dominantCol.add(col - 'A' + 1);
        }
        return dominantCol;
    }

    private int findIndex(Character last) {
        for (int i = 0; i < colsInRange.size(); i++){
            if (colsInRange.get(i) == last){
                return i;
            }
        }
        return -1;
    }

    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }


    private boolean isRangeValid(){
        String regex = "^[A-Z]\\d+\\.\\.[A-Z]\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(theRange.getText().toUpperCase());
        return matcher.matches();
    }

}