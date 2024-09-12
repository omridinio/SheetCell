package Components.Commands.Filter;

import Components.Cell.CellContoller;
import Components.Commands.CheckRange.CheckRangeController;
import Components.Commands.Commands;
import Components.Commands.CommandsController;
import Components.Commands.Filter.FilterColums.FilterColumsController;
import Components.Error.ErrorController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterController implements Commands {

    private CommandsController commandsController;

    @FXML
    private Button addLevelButtom;

    @FXML
    private FlowPane colsChoose;

    @FXML
    private Button deleteLevel;

    @FXML
    private AnchorPane checkRange;

    @FXML
    private CheckRangeController checkRangeController;

    @FXML
    private Button OK;

    private List<Integer> colsRange;

    private List<FilterColumsController> filterColumsControllers = new ArrayList<>();


    public void initialize() {
        OK.setDisable(true);
        addLevelButtom.setDisable(true);
        deleteLevel.setDisable(true);
        if(checkRangeController != null){
            checkRangeController.setCommands(this);
        }

    }


    @FXML
    void addLevelClicked(ActionEvent event) throws IOException {
        filterColumsControllers.get(filterColumsControllers.size() - 1).turnOf();
        createNewFilterColumsController();
        addLevelButtom.setDisable(true);

    }

    @FXML
    void deleteLevelClicked(ActionEvent event) {
        filterColumsControllers.removeLast();
        filterColumsControllers.get(filterColumsControllers.size() - 1).turnOn();
        addLevelButtom.setDisable(false);
    }

    @FXML
    void okClicked(ActionEvent event) {

    }

    @Override
    public void VClicked() throws IOException {
        if(checkRangeController.isRangeValid()) {
            try {
                colsRange = commandsController.Vclicked(checkRangeController.getTheRange().getText().toUpperCase());
                createNewFilterColumsController();
            } catch (Exception e) {
                ErrorController.showError(e.getMessage());
                e.printStackTrace();
            }
        }
        checkRangeController.getErrorMessege().setVisible(!checkRangeController.isRangeValid());
    }

    @Override
    public void XClicked() {

    }

    private List<Integer> rowSelected(){
        List<Integer> rowsSelected = filterColumsControllers.get(0).getRowSelcted();
        for (FilterColumsController filterColumsController : filterColumsControllers) {
            if(filterColumsController != filterColumsControllers.get(0) && filterColumsController != filterColumsControllers.get(filterColumsControllers.size() - 1)){
                List<Integer> rowSelcted = filterColumsController.getRowSelcted();
                rowsSelected.retainAll(rowSelcted);
            }
        }
        return rowsSelected;
    }

    private void createNewFilterColumsController() throws IOException {
        ChoiceBox<Character> choiceBox = checkRangeController.createFirstChoiceBox(colsRange);
        if (!choiceBox.getItems().isEmpty()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Filter/FilterColums/filterColums.fxml"));
            Node filterCol = loader.load();
            FilterColumsController filterColumsController = loader.getController();
            filterColumsController.setFilterController(this);
            filterColumsController.setColums(choiceBox);
            filterColumsControllers.add(filterColumsController);
            colsChoose.getChildren().add(filterCol);
            //checkRangeController.initPart2(this.OK, this.addLevelButtom);
        }
    }




    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }

    public Map<Integer, String> getItemInColum(Character value) {
        if(filterColumsControllers.size() == 1){
            return getItemInColumFirst(value);
        }
        else {
            return getItemInColumAfter(value);
        }
    }

    private Map<Integer, String> getItemInColumFirst(Character value){
        String theRange = checkRangeController.getTheRange().getText().toUpperCase();
        Map<Integer, String> item = commandsController.getItemInColum(value, theRange);
        return item;
    }

    private Map<Integer, String> getItemInColumAfter(Character value){
        String theRange = checkRangeController.getTheRange().getText().toUpperCase();
        List<Integer> rowSelected = rowSelected();
        Map<Integer, String> item = commandsController.getItemInColum(value, theRange, rowSelected);
        return item;
    }

    public int getFirstRowInRane(){
        return checkRangeController.getFirstRowInRane();
    }

    public void addLevelOn() {
        addLevelButtom.setDisable(false);
    }
}




