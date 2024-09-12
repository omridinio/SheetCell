package Components.Commands.CheckRange;

import Components.Commands.Commands;
import Components.Error.ErrorController;
import expression.Range;
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
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckRangeController {

    @FXML
    private VBox errorMessege;

    @FXML
    private TextField theRange;

    @FXML
    private Button vButton;

    @FXML
    private Button xButton;

    private Commands commands;

    private List<Character> colsInRange = new ArrayList<>();

    private ObservableList<ChoiceBox<Character>> choiceBoxes = FXCollections.observableArrayList();

    private BooleanBinding anyEmptyOrUnselectedBinding;




    @FXML
    void VClicked(ActionEvent event) throws IOException, ClassNotFoundException {
        commands.VClicked();
    }

    @FXML
    void XClicked(ActionEvent event) {
        colsInRange.clear();
        choiceBoxes.clear();
        commands.XClicked();
    }

    public ChoiceBox<Character> createFirstChoiceBox(List<Integer> colsRange) throws IOException {
        ChoiceBox<Character> choiceBox = new ChoiceBox<>();
        try {
            for (int i = colsRange.get(0); i <= colsRange.get(1); i++){
                char col = (char) (i - 1 + 'A');
                choiceBox.getItems().add(col);
                colsInRange.add(col);
            }
            choiceBoxes.add(choiceBox);
            vButton.setDisable(true);
            xButton.setDisable(false);
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }
        return choiceBox;
    }

    public ChoiceBox<Character> addLevelClicked() {
        ChoiceBox<Character> newChoiceBox = new ChoiceBox<Character>();
        for (char col : colsInRange){
            newChoiceBox.getItems().add(col);
        }
        choiceBoxes.add(newChoiceBox);
        newChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            anyEmptyOrUnselectedBinding.invalidate();
        });
        return newChoiceBox;
    }

    public boolean deleteLevelClicked() {
        if (choiceBoxes.size() > 1){
            choiceBoxes.remove(choiceBoxes.size() - 1);
        }
        if (choiceBoxes.size() == 1){
            return true;
        }
        return false;
    }

    public List<Integer> createDominantCols() {
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

    public VBox getErrorMessege() {
        return errorMessege;
    }

    public TextField getTheRange() {
        return theRange;
    }

    public Button getVButton() {
        return vButton;
    }

    public Button getXButton() {
        return xButton;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public Commands getCommands() {
        return commands;
    }

    public boolean isRangeValid(){
        String regex = "^[A-Z]\\d+\\.\\.[A-Z]\\d+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(theRange.getText().toUpperCase());
        return matcher.matches();
    }

    public void initPart2(Button OK, Button addLevelButtom) {
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

    public int getFirstRowInRane() {
        return Integer.parseInt(theRange.getText().substring(1, theRange.getText().indexOf('.')));
    }





}
