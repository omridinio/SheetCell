package Components.Shitcell;

import Components.ActionLine.ActionLineController;
import Components.Commands.CommandsController;
import Components.Error.ErrorController;
import Components.RangeArea.RangeAreaController;
import Components.StyleSheet.StyleSheetController;
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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.*;

import Components.Cell.CellContoller;

public class ShitsellController {

    //fmxl id
    @FXML
    private AnchorPane actionLine;

    @FXML private ActionLineController actionLineController;

    @FXML
    private AnchorPane rangeArea;

    @FXML
    private RangeAreaController rangeAreaController;

    @FXML
    private TextField filePath;

    @FXML
    private GridPane sheet;

    @FXML
    AnchorPane styleSheet;

    @FXML
    StyleSheetController styleSheetController;

    @FXML
    VBox sheetArea;

    @FXML
    AnchorPane commandArea;

    @FXML
    private CommandsController commandAreaController;

    @FXML
    private Button readOnlyMode;



    //my dataMember
    private CellUI currCell;
    private BooleanProperty isReadOnlyMode = new SimpleBooleanProperty(false);
    private BooleanProperty isdeleteRangeMode = new SimpleBooleanProperty(false);
    Map<Coordinate,CellContoller> coordToController = new HashMap<>();
    Map<Coordinate,CellContoller> backupCoordToController = new HashMap<>();
    private Logic logic = new ImplLogic();
    private BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private Button currRange;
    private List<CellContoller> cellsDependOnThem = new ArrayList<>();
    private List<CellContoller> getCellsDependOnhim = new ArrayList<>();
    Tooltip actionMessege = new Tooltip();
    private List<CellContoller> cellChoosed = new ArrayList<>();
    private Set<Coordinate> readOnlyCoord;
    //private Map<Coordinate, CellDTO> sortRange;


    public ShitsellController() {
        currCell = new CellUI();
    }

    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
        if(actionLineController != null) {
            actionLineController.setShitsellController(this);
            actionLineController.initializeActionLine();
        }
        if(rangeAreaController != null) {
            rangeAreaController.setShitsellController(this);
            rangeAreaController.initializeRangeArea();
        }
        if (styleSheetController != null) {
            styleSheetController.setShitsellController(this);
            styleSheetController.initializeStyleSheet();
        }

        if (commandAreaController != null) {
            commandAreaController.setShitsellController(this);
            commandAreaController.initializeCommands();
        }
        readOnlyMode.disableProperty().bind(isReadOnlyMode.not());
        readOnlyMode.visibleProperty().bind(isReadOnlyMode);
        sheet.disableProperty().bind(isdeleteRangeMode);
    }

    public GridPane getSheet() {
        return sheet;
    }

    public VBox getSheetArea() {
        return sheetArea;
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            File file = fileChooser.showOpenDialog(null);
            restSheet();
            logic.creatNewSheet(file.getAbsolutePath());
            isLoaded.setValue(true);
            int row = logic.getSheet().getRowCount();
            int col = logic.getSheet().getColumnCount();
            int width = logic.getSheet().getWidth();
            int height = logic.getSheet().getThickness();
            createEmptySheet(col, row, width, height);
            updateSheet(logic.getSheet());
            createRanges();
            filePath.setText(file.getAbsolutePath());
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    @FXML
    private void closeReadOnlyCllicked() throws IOException, ClassNotFoundException {
        for (Coordinate coordinate : readOnlyCoord){
            switchCells(coordToController.get(coordinate), backupCoordToController.get(coordinate));
        }
        updateCells();
        backupCoordToController.clear();
        readOnlyCoord.clear();
        isReadOnlyMode.setValue(false);
    }

    private void createRanges() throws IOException {
        List<String> ranges = logic.getRangesName();
        for (String range : ranges) {
            try {
                rangeAreaController.addRange(range, logic.getRange(range));
            } catch (IOException e) {
                ErrorController.showError(e.getMessage());
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

    private void createEmptySheet(int col, int row, int widthCell, int heightCell) throws IOException {
        sheet.getRowConstraints().clear();
        sheet.getColumnConstraints().clear();
        for(int i = 0; i <= row; i++){
            for(int j = 0; j <= col; j++){
                //RowConstraints rowConstraints = new RowConstraints();
                //ColumnConstraints columnConstraints = new ColumnConstraints();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/cell.fxml"));
                Node cell = loader.load();
                CellContoller cellContoller = loader.getController();
                cellContoller.setShitsellController(this);
                //cellContoller.setColumnConstraints(columnConstraints);
                //cellContoller.setRowConstraints(rowConstraints);
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                if(i == 0 && j == 0){
                    RowConstraints rowConstraints = new RowConstraints();
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    cellContoller.setColumnConstraints(columnConstraints);
                    cellContoller.setRowConstraints(rowConstraints);
                    cellContoller.getCell().setDisable(true);
                    rowConstraints.setMinHeight(40);
                    rowConstraints.setPrefHeight(20);
                    columnConstraints.setMinWidth(40);
                    columnConstraints.setPrefWidth(100);
                    cell.getStyleClass().add("empty");
                    sheet.getRowConstraints().add(rowConstraints);
                    sheet.getColumnConstraints().add(columnConstraints);
                    cellContoller.setBackgroundColor("737370");
                    //cellContoller.ableColSpearte();
                    //cellContoller.ableRowSpearte();
                }
                else if(i == 0){
                    cellContoller.setIsTitle(true);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    cellContoller.setColumnConstraints(columnConstraints);
                    //cellContoller.getCell().setDisable(true);
                    columnConstraints.setPrefWidth(widthCell);
                    cellContoller.setText(String.valueOf((char)('A' + j - 1)));
                    cell.getStyleClass().add("ABC");
                    cellContoller.ableColSpearte();
                    sheet.getColumnConstraints().add(columnConstraints);
                    cellContoller.setBackgroundColor("737370");
                }
                else if(j == 0){
                    RowConstraints rowConstraints = new RowConstraints();
                    cellContoller.setRowConstraints(rowConstraints);
                    cellContoller.getCell().setDisable(true);
                    rowConstraints.setPrefHeight(heightCell);
                    cell.getStyleClass().add("number");
                    String s = i < 10 ? "0" + i : String.valueOf(i);
                    cellContoller.setText(s);
                    cellContoller.ableRowSpearte();
                    sheet.getRowConstraints().add(rowConstraints);
                    cellContoller.setBackgroundColor("737370");
                }
                else{
                    cell.getStyleClass().add("cell");
                    cell.setId(String.valueOf((char)('A' + j - 1)) + i);
                    Coordinate coordinate = new CoordinateImpl(i, j);
                    coordToController.put(coordinate, cellContoller);
                    //cellContoller.getCell().setStyle("-fx-background-color: white");
                }
                cell.prefWidth(widthCell);
                cell.prefHeight(heightCell);
//                sheet.getRowConstraints().add(rowConstraints);
//                sheet.getColumnConstraints().add(columnConstraints);
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

    public void titleClicked(CellContoller cellContoller) {
        clearCurrCell();
        clearCellsMark();
        clearCellChoosed();
        String col = cellContoller.getText();
        for (int i = 1; i <= logic.getSheet().getRowCount(); i++) {
            String cellId = col + i;
            Coordinate coordinate = new CoordinateImpl(cellId);
            CellContoller cell = coordToController.get(coordinate);
            cell.getCell().getStyleClass().add("colSelect");
            cell.setSelect();
            cellChoosed.add(cell);
        }
    }

    public void cellClicked(CellDTO cell, Button button) {
        clearCellsMark();
        clearCellChoosed();
        clearCurrCell();
        currCell.clickedCell.getStyleClass().remove("clicked");
        currCell.cellid.setValue(cell.getId());
        currCell.originalValue.setValue(cell.getOriginalValue());
        currCell.lastVersion.setValue(String.valueOf(cell.getLastVersionUpdate()));
        currCell.clickedCell = button;
        currCell.isClicked.setValue(true);
        currCell.clickedCell.getStyleClass().add("clicked");
        currCell.cellContoller = coordToController.get(new CoordinateImpl(cell.getId()));
        styleSheetController.updateStyleSheet(currCell.cellContoller);
        List<Coordinate> coordCellDependOfThem = cell.getCellsDependsOnThem();
        for (Coordinate coordinate : coordCellDependOfThem) {
            coordToController.get(coordinate).getCell().getStyleClass().add("dependThem");
            coordToController.get(coordinate).setDependOnThem();
            cellsDependOnThem.add(coordToController.get(coordinate));
        }
        List<Coordinate> coordCellDependOfHim = cell.getCellsDependsOnHim();
        for (Coordinate coordinate : coordCellDependOfHim) {
            coordToController.get(coordinate).getCell().getStyleClass().add("dependHim");
            coordToController.get(coordinate).setDependOnHim();
            getCellsDependOnhim.add(coordToController.get(coordinate));
        }
    }

    public void cellIdEnter(TextField cellId) {
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

    public void updateCellClicked(TextField actionLine, TextField cellId) throws IOException {
        try {
            logic.updateCell(currCell.cellid.getValue(), actionLine.getText());
            updateSheet(logic.getSheet());
            CellDTO currCell = logic.getCell(new CoordinateImpl(cellId.getText()));
            cellClicked(currCell, coordToController.get(new CoordinateImpl(cellId.getText())).getCell());
            actionLine.setText("");
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    public void setRange(String rangeId, String theRange) throws IOException {
        try{
            logic.createNewRange(rangeId, theRange);
            rangeAreaController.addRange(rangeId, logic.getRange(rangeId));
        } catch (IOException e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }


    }

    public void rangeClicked(RangeDTO rangeDTO, Button range, FlowPane rangeArea) {
        clearCellsMark();
        clearCellChoosed();
        rangeArea.requestFocus();
        List<CellDTO> cells = rangeDTO.getRangeCells();
        for (CellDTO cell : cells) {
            coordToController.get(new CoordinateImpl(cell.getId())).getCell().getStyleClass().add("dependThem");;
            coordToController.get(new CoordinateImpl(cell.getId())).setDependOnThem();
            cellsDependOnThem.add(coordToController.get(new CoordinateImpl(cell.getId())));
        }
        currRange = range;
        currRange.getStyleClass().add("clicked");
    }

    private void clearCellsMark(){
        for (CellContoller cell : cellsDependOnThem) {
            cell.getCell().getStyleClass().clear();
            cell.getCell().getStyleClass().add("button");
            cell.updateCellDeatils();
        }
        cellsDependOnThem.clear();
        for (CellContoller cell : getCellsDependOnhim) {
            cell.getCell().getStyleClass().clear();
            cell.getCell().getStyleClass().add("button");
            cell.updateCellDeatils();
        }
        getCellsDependOnhim.clear();
    }

    public void intitializeActionLine(ActionLineController actionLineController) {
        actionLineController.getOriginalValue().textProperty().bind(currCell.originalValue);
        actionLineController.getLastVersion().textProperty().bind(currCell.lastVersion);
        actionLineController.getActionLine().disableProperty().bind(currCell.isClicked.not().and(isReadOnlyMode));
        actionLineController.getActionLine().editableProperty().bind(currCell.isClicked);
        actionLineController.getCellId().disableProperty().bind(isLoaded.not());
        actionLineController.getCellId().editableProperty().bind(isLoaded);
        actionLineController.getLastVersion().disableProperty().bind(isLoaded.not());
        actionLineController.getOriginalValue().disableProperty().bind(isLoaded.not());
        actionLineController.getUpdateValue().disableProperty().bind(isReadOnlyMode);
        actionLineController.getCellId().textProperty().bind(currCell.cellid);
        actionLineController.getCellId().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                actionLineController.getCellId().textProperty().bind(currCell.cellid);
            }
            else{
                actionLineController.getCellId().textProperty().unbind();
            }
        });
    }

    public void intitializeRangeArea(RangeAreaController rangeAreaController) {
        rangeAreaController.getRangeArea().setFocusTraversable(true);
        rangeAreaController.getRangeAreaController().visibleProperty().bind(isLoaded);
        rangeAreaController.getRangeArea().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                if (currRange != null)
                    currRange.getStyleClass().remove("clicked");
                clearCellsMark();
            }
        });
        rangeAreaController.getAddRange().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode));
        rangeAreaController.getDeleteRange().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode));
        rangeAreaController.getSumbitDelete().visibleProperty().bind(isdeleteRangeMode);
        rangeAreaController.getCancel().visibleProperty().bind(isdeleteRangeMode);
    }

    private void clearCellChoosed(){
        for (CellContoller cell : cellChoosed) {
            cell.getCell().getStyleClass().clear();
            cell.getCell().getStyleClass().add("button");
            cell.updateCellDeatils();
        }
        cellChoosed.clear();
    }

    private void clearCurrCell(){
        if (currCell.cellContoller != null)
            currCell.cellContoller.updateCellDeatils();
        currCell.cellid.setValue("");
        currCell.originalValue.setValue("");
        currCell.lastVersion.setValue(String.valueOf(""));
        currCell.clickedCell.getStyleClass().remove("clicked");
        currCell.isClicked.setValue(false);
        currCell.cellContoller = null;
    }

    public void bootomClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setHigh("bottom");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setHigh("bottom");
            currCell.cellContoller.calculatecoord();
        }

    }

    public void rightClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setWidth("right");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setWidth("right");
            currCell.cellContoller.calculatecoord();
        }
    }

    public void leftClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setWidth("left");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setWidth("left");
            currCell.cellContoller.calculatecoord();
        }
    }

    public void topClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setHigh("top");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setHigh("top");
            currCell.cellContoller.calculatecoord();
        }
    }

    public void centerClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setWidth("center");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setWidth("center");
            currCell.cellContoller.calculatecoord();
        }
    }

    public void midlleClicked() {
        for (CellContoller cell : cellChoosed) {
            cell.setHigh("center");
            cell.calculatecoord();
        }
        if (currCell.cellContoller != null) {
            currCell.cellContoller.setHigh("center");
            currCell.cellContoller.calculatecoord();
        }
    }

    public void changeSizeFont(int size) {
        for (CellContoller cell : cellChoosed) {
            cell.setSizeFont(size);
        }
        if (currCell.cellContoller != null)
            currCell.cellContoller.setSizeFont(size);
    }

    public void changeFontColor(Color value) {
        if (cellChoosed != null) {
            for (CellContoller cell : cellChoosed) {
                //cell.getCell().setStyle("-fx-text-fill: #" + value.toString().substring(2));
                cell.setFontColor(value.toString().substring(2));

            }
        }
        //currCell.clickedCell.setStyle("-fx-text-fill: #" + value.toString().substring(2));
        if (currCell.cellContoller != null)
            currCell.cellContoller.setFontColor(value.toString().substring(2));
    }

    public void backgroundColorChange(Color value) {
        for (CellContoller cell : cellChoosed) {
            cell.setBackgroundColor(value.toString().substring(2));
        }
        if (currCell.cellContoller != null)
            currCell.cellContoller.setBackgroundColor(value.toString().substring(2));
    }

    public void changeFontType(String value) {
        if (cellChoosed != null) {
            for (CellContoller cell : cellChoosed) {
                cell.setFontType(value);
            }
        }
        if (currCell.cellContoller != null)
            currCell.cellContoller.setFontType(value);
    }

    public void intitializeStyleSheet(StyleSheetController styleSheetController) {
        styleSheetController.getStyleSheet().visibleProperty().bind(isLoaded);
        styleSheetController.getStyleSheet().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode));
    }

    public void initializeCommands(CommandsController commandsController) {
        //commandsController.getCommandArea().visibleProperty().bind(isLoaded);
        //commandsController.getCommandArea().disableProperty().bind(isReadOnlyMode);
    }

    public void sortRangeClicked(String range, List<Integer> dominantCols) throws IOException, ClassNotFoundException {
        Map<Coordinate, CellDTO> sortRange = logic.getSortRange(range, dominantCols);
        createReadOnlySheet(sortRange);
        updateCells();
        readOnlyCoord = sortRange.keySet();
        modeReadOnly();
    }

    private void modeReadOnly() {
        isReadOnlyMode.setValue(true);
    }

    private void updateCells(){
        for (Coordinate coordinate : coordToController.keySet()){
            coordToController.get(coordinate).updateCellDeatils();
            coordToController.get(coordinate).calculatecoord();
        }
    }

    public List<Integer> getColsInRange(String range){
        return logic.getTheRangeOfTheRange(range);
    }

    private void createReadOnlySheet(Map<Coordinate, CellDTO>sortRange) throws IOException, ClassNotFoundException {
        for (Coordinate coordinate : coordToController.keySet()){
            backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
        }
        for (Coordinate coordinate : sortRange.keySet()){
            switchCells(coordToController.get(coordinate), backupCoordToController.get((new CoordinateImpl(sortRange.get(coordinate).getId()))));
        }
    }

    private CellContoller createNewCell(Coordinate coordinate) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/Cell.fxml"));
        Node cell = loader.load();
        CellContoller cellContoller = loader.getController();
        cellContoller.setShitsellController(this);
        cellContoller.copyCell(coordToController.get(coordinate));
        return cellContoller;
    }

    private void switchCells(CellContoller cell1, CellContoller cell2) throws IOException, ClassNotFoundException {
        cell1.copyCell(cell2);
    }

    public void deleteRangeModeOn() {
        isdeleteRangeMode.setValue(true);
    }

    public void deleteRange(String rangeId) {
        logic.removeRange(rangeId);
    }

    public void deleteRangeModeOff() {
        isdeleteRangeMode.setValue(false);
    }

    public Map<Integer, String> getColumsItem(int col, String theRange){
        Map<Integer, String> itemsInRow = new HashMap<>();
        List<Coordinate> coordinatesOfRange = logic.getCoordinateInRange(theRange);
        for (Coordinate coordinate : coordinatesOfRange) {
            if (coordinate.getColumn() == col) {
                itemsInRow.put(coordinate.getRow(), coordToController.get(coordinate).getCell().getText());
            }
        }
        return itemsInRow;
    }

    public Map<Integer, String> getColumsItem(int i, String theRange, List<Integer> rowSelected) {
        Map<Integer, String> itemsInRow = new HashMap<>();
        List<Coordinate> coordinatesOfRange = logic.getCoordinateInRange(theRange);
        for (Coordinate coordinate : coordinatesOfRange) {
            if (coordinate.getColumn() == i && rowSelected.contains(coordinate.getRow())) {
                itemsInRow.put(coordinate.getRow(), coordToController.get(coordinate).getCell().getText());
            }
        }
        return itemsInRow;
    }

    public void filterOkClicked(List<Integer> rowSelected, String theRange) throws IOException, ClassNotFoundException {
        int firstRowInRange = Integer.parseInt(theRange.substring(1, theRange.indexOf('.')));
        int firstColInRange = theRange.charAt(0) - 'A' + 1;
        //int lastRowInRange = Integer.parseInt(theRange.substring(theRange.indexOf('.') + 1), theRange.length());
        int lastColInRange = theRange.charAt(theRange.length() - 2) - 'A' + 1;
        List<Coordinate> coordinatesOfRange = logic.getCoordinateInRange(theRange);
        readOnlyCoord = new HashSet<>(coordinatesOfRange);
        rowSelected.sort(Comparator.naturalOrder());
        for (Coordinate coordinate : coordToController.keySet()) {
            backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
        }
        int currRow = firstRowInRange;
        for (int row : rowSelected) {
            for (int col = firstColInRange; col <= lastColInRange; col++) {
                Coordinate prevCoordinate = new CoordinateImpl(row, col);
                Coordinate newCoordinate = new CoordinateImpl(currRow, col);
                switchCells(coordToController.get(newCoordinate), backupCoordToController.get(prevCoordinate));
                coordinatesOfRange.remove(newCoordinate);
            }
            currRow++;
        }
        for (Coordinate coordinate : coordinatesOfRange) {
           coordToController.get(coordinate).restCell();
        }
        updateCells();
        modeReadOnly();
    }
}

