package Components.Shitcell;

import Components.Range.RangeController;
import Components.Range.setRange.setRangeController;
import Properties.CellUI;
import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.RangeDTO;

import jakarta.xml.bind.JAXBException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Components.Cell.CellContoller;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShitsellController {

    //fmxl id
    @FXML
    private TextField actionLine;

    @FXML
    private TextField cellId;

    @FXML
    private TextField lastVersion;

    @FXML
    private TextField originalValue;

    @FXML
    private GridPane sheet;

    @FXML
    private ComboBox<?> versionSelctor;

    @FXML
    private TextField filePath;

    @FXML
    private Button updateValue;

    @FXML
    private FlowPane rangeArea;

    @FXML
    private VBox rangeAreaController;



    //my dataMember
    private CellUI currCell;
    Map<Coordinate,CellContoller> coordToController = new HashMap<>();
    Map<String, RangeController> rangeToController = new HashMap<>();
    private Logic logic = new ImplLogic();
    private BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private Button currRange;
    private List<CellContoller> cellsDependOnThem = new ArrayList<>();
    private List<CellContoller> getCellsDependOnhim = new ArrayList<>();


    public ShitsellController() {
        currCell = new CellUI();
    }

    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
        rangeArea.setFocusTraversable(true);
        originalValue.textProperty().bind(currCell.originalValue);
        lastVersion.textProperty().bind(currCell.lastVersion);
        actionLine.disableProperty().bind(currCell.isClicked.not());
        actionLine.editableProperty().bind(currCell.isClicked);
        cellId.disableProperty().bind(isLoaded.not());
        cellId.editableProperty().bind(isLoaded);
        lastVersion.disableProperty().bind(isLoaded.not());
        originalValue.disableProperty().bind(isLoaded.not());
        cellId.textProperty().bind(currCell.cellid);
        rangeAreaController.visibleProperty().bind(isLoaded);
        cellId.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
               cellId.textProperty().bind(currCell.cellid);
            }
            else{
                cellId.textProperty().unbind();
            }
        });
        updateValue.disableProperty().bind(currCell.isClicked.not());
        rangeArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if (currRange != null)
                    currRange.getStyleClass().remove("clicked");
                clearCellsMark();
            }
        });

    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        restSheet();
        logic.creatNewSheet(file.getAbsolutePath());
        isLoaded.setValue(true);
        int row = logic.getSheet().getRowCount();
        int col = logic.getSheet().getColumnCount();
        int width = logic.getSheet().getThickness();
        int height = logic.getSheet().getRowCount();
        createEmptySheet(col, row, width, height);
        updateSheet(logic.getSheet());
        createRanges();
        filePath.setText(file.getAbsolutePath());
    }

    private void createRanges(){
        List<String> ranges = logic.getRangesName();
        for (String range : ranges) {
            try {
                addRange(range);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateSheet(SheetDTO sheet){
        for (int i = 1; i <= sheet.getRowCount(); i++) {
            for (int j = 1; j <= sheet.getColumnCount(); j++) {
                Coordinate coordinate = new CoordinateImpl(i, j);
                coordToController.get(coordinate).setCellDTO(logic.getCell(coordinate));
            }
        }
    }

    // Method to dynamically add buttons
    private void createEmptySheet(int col, int row, int widthCell, int heightCell) throws IOException {
        sheet.getRowConstraints().clear();
        sheet.getColumnConstraints().clear();
        for(int i = 0; i <= row; i++){
            for(int j = 0; j <= col; j++){
                RowConstraints rowConstraints = new RowConstraints();
                ColumnConstraints columnConstraints = new ColumnConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/cell.fxml"));
                Node cell = loader.load();
                CellContoller cellContoller = loader.getController();
                cellContoller.setShitsellController(this);
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                if(i == 0 && j == 0){
                    cellContoller.getCell().setDisable(true);
                    rowConstraints.setPrefHeight(20);
                    columnConstraints.setPrefWidth(45);
                    cell.getStyleClass().add("empty");
                }
                else if(i == 0){
                    cellContoller.getCell().setDisable(true);
                    //columnConstraints.setPrefWidth(widthCell);
                    cellContoller.setText(String.valueOf((char)('A' + j - 1)));
                    cell.getStyleClass().add("ABC");
                }
                else if(j == 0){
                    cellContoller.getCell().setDisable(true);
                    //rowConstraints.setPrefHeight(heightCell);
                    cell.getStyleClass().add("number");
                    String s = i < 10 ? "0" + i : String.valueOf(i);
                    cellContoller.setText(s);
                }
                else{
                    cell.getStyleClass().add("cell");
                    cell.setId(String.valueOf((char)('A' + j - 1)) + i);
                    Coordinate coordinate = new CoordinateImpl(i, j);
                    coordToController.put(coordinate, cellContoller);
                }
                sheet.getRowConstraints().add(rowConstraints);
                sheet.getColumnConstraints().add(columnConstraints);
                sheet.add(cell, j, i);
            }
        }


    }

    private void restSheet(){
        if(sheet != null)
            sheet.getChildren().clear();
        currCell.cellid.setValue("");
        currCell.originalValue.setValue("");
        currCell.lastVersion.setValue(String.valueOf(""));
        coordToController.clear();
    }

    public void cellClicked(CellDTO cell, Button button) {
        clearCellsMark();
        currCell.clickedCell.getStyleClass().remove("clicked");
        currCell.cellid.setValue(cell.getId());
        currCell.originalValue.setValue(cell.getOriginalValue());
        currCell.lastVersion.setValue(String.valueOf(cell.getLastVersionUpdate()));
        currCell.clickedCell = button;
        currCell.isClicked.setValue(true);
        currCell.clickedCell.getStyleClass().add("clicked");
        List<Coordinate> coordCellDependOfThem = cell.getCellsDependsOnThem();
        for (Coordinate coordinate : coordCellDependOfThem) {
            coordToController.get(coordinate).getCell().getStyleClass().add("dependThem");
            cellsDependOnThem.add(coordToController.get(coordinate));
        }
        List<Coordinate> coordCellDependOfHim = cell.getCellsDependsOnHim();
        for (Coordinate coordinate : coordCellDependOfHim) {
            coordToController.get(coordinate).getCell().getStyleClass().add("dependHim");
            getCellsDependOnhim.add(coordToController.get(coordinate));
        }
    }

    @FXML
    void cellIdEnter(ActionEvent event) {
        String id = cellId.getText().toUpperCase();
        if(validInputCell(id)){
            Coordinate coordinate = new CoordinateImpl(id);
            try {
                CellDTO cell = logic.getCell(coordinate);
                cellClicked(cell, coordToController.get(coordinate).getCell());
                cellId.setText(id);
            } catch (Exception e) { }
        }
        else { }
    }

    public boolean validInputCell(String input){
        if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {
            String temp = input.substring(1);
            try {
                if(Integer.parseInt(temp) > 0) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    @FXML
    void updateCellClicked(ActionEvent event) {
        try {
            logic.updateCell(currCell.cellid.getValue(), actionLine.getText());
            updateSheet(logic.getSheet());
            CellDTO currCell = logic.getCell(new CoordinateImpl(cellId.getText()));
            cellClicked(currCell, coordToController.get(new CoordinateImpl(cellId.getText())).getCell());
            actionLine.setText("");
        } catch (NumberFormatException e) {

        } catch (Exception e) {

        }
    }

    @FXML
    void addRangeClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/setRange/setRange.fxml"));
        Parent newWindowRoot = loader.load();
        setRangeController rangeController = loader.getController();
        rangeController.setShitsellController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("Add new Range");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
    }

    public void setRange(String rangeId, String theRange) throws IOException {
        try{
            logic.createNewRange(rangeId, theRange);
            addRange(rangeId);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void addRange(String rangeId) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Range/Range.fxml"));
        Node range = loader.load();
        RangeController rangeContoller = loader.getController();
        rangeContoller.setRangeDTO(logic.getRange(rangeId));
        rangeContoller.setShitsellController(this);
        rangeToController.put(rangeId, rangeContoller);
        rangeArea.getChildren().add(range);
    }

    public void rangeClicked(RangeDTO rangeDTO, Button range) {
        clearCellsMark();
        rangeArea.requestFocus();
        List<CellDTO> cells = rangeDTO.getRangeCells();
        for (CellDTO cell : cells) {
            coordToController.get(new CoordinateImpl(cell.getId())).getCell().getStyleClass().add("dependThem");
            cellsDependOnThem.add(coordToController.get(new CoordinateImpl(cell.getId())));
        }
        currRange = range;
        currRange.getStyleClass().add("clicked");
    }

    private void clearCellsMark(){
        for (CellContoller cell : cellsDependOnThem) {
            cell.getCell().getStyleClass().clear();
            cell.getCell().getStyleClass().add("button");
        }
        cellsDependOnThem.clear();
        for (CellContoller cell : getCellsDependOnhim) {
            cell.getCell().getStyleClass().clear();
            cell.getCell().getStyleClass().add("button");
        }
    }
}

