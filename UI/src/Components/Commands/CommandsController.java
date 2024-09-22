package Components.Commands;

import Components.Commands.Expression.ExpressionController;
import Components.Commands.Filter.FilterController;
import Components.Commands.Graph.GraphController;
import Components.Commands.SetCommand.SetCommandController;
import Components.Range.setRange.setRangeController;
import Components.Shitcell.ShitsellController;
import Properties.ExpressionUi;
import expression.api.EffectiveValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommandsController {

    @FXML
    private Button sort;

    @FXML
    private Button filter;

    private ShitsellController shitsellController;

    @FXML
    private Button dynmicAnlyzeForCell;

    @FXML
    private Button customFormula;


    @FXML
    void clickeSort(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/SetCommand/SetCommand.fxml"));
        Parent newWindowRoot = loader.load();
        SetCommandController setCommandController = loader.getController();
        setCommandController.setCommandsController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Sort range of the sheet");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    @FXML
    void filterClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Filter/filter.fxml"));
        Parent newWindowRoot = loader.load();
        FilterController filterController = loader.getController();
        filterController.setCommandsController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Filter range of the sheet");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    @FXML
    void dynmicCellClicked(ActionEvent event) {
        shitsellController.dynmicAnlyzeForCell();
    }

    @FXML
    void dynmicSheetClicked(ActionEvent event) {
        shitsellController.dynmicAnlyzeForSheet();
    }

    @FXML
    void createGraphClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Graph/graph.fxml"));
        Parent newWindowRoot = loader.load();
        GraphController graphController = loader.getController();
        graphController.setCommandsController(this);
        graphController.init();
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Create Graph");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    @FXML
    void customFormulaClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Expression/expression.fxml"));
        Parent newWindowRoot = loader.load();
        ExpressionController expressionController = loader.getController();
        expressionController.setCommandsController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Custom Formula");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    public void  initialize(){
        dynmicAnlyzeForCell.setDisable(true);
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public List<Integer> Vclicked(String range) throws IOException, ClassNotFoundException {
        return shitsellController.getColsInRange(range);
    }


    public void initializeCommands(){
        shitsellController.initializeCommands(this);
    }


    public void okClicked(String theRange, List<Integer> cols) throws IOException, ClassNotFoundException {
        shitsellController.sortRangeClicked(theRange, cols);
    }

    public Button getSort() {
        return sort;
    }

    public Button getCustomFormula() {
        return customFormula;
    }


    public Map<Integer, String> getItemInColum(Character value, String theRange) {
        return shitsellController.getColumsItem(value + 1 - 'A', theRange);
    }

    public Map<Integer, String> getItemInColum(Character value, String theRange, List<Integer> rowSelected) {
        return shitsellController.getColumsItem(value + 1 - 'A', theRange, rowSelected);
    }

    public void filterOkClicked(List<Integer> rowSelected, String theRange) throws IOException, ClassNotFoundException {
        shitsellController.filterOkClicked(rowSelected, theRange);
    }

    public void disableDynmicCell() {
        dynmicAnlyzeForCell.setDisable(true);
    }

    public void enableDynmicCell() {
        dynmicAnlyzeForCell.setDisable(false);
    }

    public List<String> getRanges() {
        return shitsellController.getRanges();
    }

    public List<EffectiveValue> getRange(String text) {
        return shitsellController.getRange(text);
    }

    public List<EffectiveValue> getCustomRange(String text) {
        return shitsellController.getCustomRange(text);
    }

    public String predictCalculate(String expression) throws IOException, ClassNotFoundException {
        return shitsellController.predictCalculate(expression);
    }

    public void addExpression(String expressionUi) throws IOException {
        shitsellController.addExpression(expressionUi);
    }
}
