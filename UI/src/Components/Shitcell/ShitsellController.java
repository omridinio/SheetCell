package Components.Shitcell;

import Components.ActionLine.ActionLineController;
import Components.Commands.CommandsController;
import Components.Error.ErrorController;
import Components.LoadFile.LoadFileController;
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

import expression.api.EffectiveValue;
import jakarta.xml.bind.JAXBException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import Components.Cell.CellContoller;
import javafx.stage.Modality;
import javafx.stage.Stage;
import Properties.TaskLoadFile;


public class ShitsellController {

    //fmxl id
    @FXML
    private HBox actionLine;

    @FXML private ActionLineController actionLineController;

    @FXML
    private VBox rangeArea;

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
    VBox commandArea;

    @FXML
    private CommandsController commandAreaController;

    @FXML
    private Button readOnlyMode;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane allTheSheet;




    //my dataMember
    private CellUI currCell;
    private BooleanProperty isReadOnlyMode = new SimpleBooleanProperty(false);
    private BooleanProperty isdeleteRangeMode = new SimpleBooleanProperty(false);
    private BooleanProperty isDynmicMode = new SimpleBooleanProperty(false);
    Map<Coordinate,CellContoller> coordToController = new HashMap<>();
    List<CellContoller> numberColCell = new ArrayList<>();
    List<CellContoller> numberRowCell = new ArrayList<>();
    Map<Coordinate,CellContoller> backupCoordToController = new HashMap<>();
    private Logic logic = new ImplLogic();
    private BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private Button currRange;
    private List<CellContoller> cellsDependOnThem = new ArrayList<>();
    private List<CellContoller> getCellsDependOnhim = new ArrayList<>();
    Tooltip actionMessege = new Tooltip();
    private List<CellContoller> cellChoosed = new ArrayList<>();
    private Set<Coordinate> readOnlyCoord = new HashSet<>();
    private String lastDynmicCell = "";
    private int countDynmicAnlyze = 0;
    private List<Coordinate> daynmicCells = new ArrayList<>();
    private File file;



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
        actionLine.setDisable(true);
        double widthScrollPane = scrollPane.getWidth();
        scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > widthScrollPane) {
                allTheSheet.setPrefWidth(newVal.doubleValue() - 5);
            }
        });

        double heightScrollPane = scrollPane.getHeight();
        scrollPane.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() > heightScrollPane) {
                allTheSheet.setPrefHeight(newVal.doubleValue() - 5);
            }
        });
    }

    public GridPane getSheet() {
        return sheet;
    }


    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open XML File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
            file = fileChooser.showOpenDialog(null);
            openLoadFile();
            logic.creatNewSheet(file.getAbsolutePath());
            restSheet();

        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadFilePart2() throws IOException {
        try {
            isLoaded.setValue(true);
            int row = logic.getSheet().getRowCount();
            int col = logic.getSheet().getColumnCount();
            int width = logic.getSheet().getWidth();
            int height = logic.getSheet().getThickness();
            createEmptySheet(col, row, width, height);
            updateSheet(logic.getSheet());
            createRanges();
            filePath.setText(file.getAbsolutePath());
            actionLine.setDisable(false);
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
            e.printStackTrace();
        }
    }


    private void openLoadFile() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/LoadFile/LoadFile.fxml"));
        Parent newWindowRoot = loader.load();
        LoadFileController loadFileController = loader.getController();
        loadFileController.setShitsellController(this);
        Scene newScene = new Scene(newWindowRoot);
        Stage newWindow = new Stage();
        newWindow.setTitle("load file progress");
        newWindow.setScene(newScene);
        newWindow.initModality(Modality.APPLICATION_MODAL); // This makes the window modal
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.show();
        TaskLoadFile taskLoadFile = new TaskLoadFile();
        loadFileController.bindProperty(taskLoadFile);
        new Thread(taskLoadFile).start();
        taskLoadFile.setOnSucceeded(workerStateEvent -> {
            newWindow.close(); // Close the window when the task completes
            try {
                loadFilePart2();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
        actionLine.setDisable(false);
        actionLineController.getVersionSelctor().setValue(actionLineController.getVersionSelctor().getItems().getLast());
        if(isDynmicMode.getValue()){
            for (int i = 0; i< countDynmicAnlyze; i++){
                logic.deleteSheet();

            }
            for(Coordinate coordinate : daynmicCells){
                coordToController.get(coordinate).turnOffDynmicAnlayze();
            }
            daynmicCells.clear();
            if(currCell.cellContoller != null){
                currCell.cellContoller.turnOffDynmicAnlayze();
            }
            isDynmicMode.setValue(false);
            updateSheet(logic.getSheet());
            lastDynmicCell = "";
            countDynmicAnlyze = 0;

        }
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
                coordToController.get(coordinate).setCellDTO(sheet.getCell(coordinate));
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
                    numberColCell.add(cellContoller);
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
                    //cellContoller.turnOnBotoomSperator();
                    sheet.getRowConstraints().add(rowConstraints);
                    cellContoller.setBackgroundColor("737370");
                    numberRowCell.add(cellContoller);
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
        backupCoordToController.clear();
        isLoaded.setValue(false);
        actionLine.setDisable(true);
        rangeAreaController.rest();
        actionLineController.rest();
        isReadOnlyMode.setValue(false);
        isdeleteRangeMode.setValue(false);
        isDynmicMode.setValue(false);
        lastDynmicCell = "";
        countDynmicAnlyze = 0;
        daynmicCells.clear();
        numberRowCell.clear();
        numberColCell.clear();
        readOnlyCoord.clear();
        cellChoosed.clear();
    }

    public void titleClicked(CellContoller cellContoller) {
        if(!isDynmicMode.getValue()) {
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
    }

    public void cellClicked(CellDTO cell, Button button) {
        if(!isDynmicMode.getValue()) {
            clearCellsMark();
            clearCellChoosed();
            clearCurrCell();
            actionLineController.getActionLine().setText("");
            currCell.clickedCell.getStyleClass().remove("clicked");
            currCell.cellid.setValue(cell.getId());
            currCell.originalValue.setValue(cell.getOriginalValue());
            currCell.lastVersion.setValue(String.valueOf(cell.getLastVersionUpdate()));
            currCell.clickedCell = button;
            currCell.isClicked.setValue(true);
            currCell.clickedCell.getStyleClass().add("clicked");
            currCell.cellContoller = coordToController.get(new CoordinateImpl(cell.getId()));
            styleSheetController.updateStyleSheet(currCell.cellContoller);
            if (currCell.cellContoller.isNaturalNumber()) {
                commandAreaController.enableDynmicCell();
            } else {
                commandAreaController.disableDynmicCell();
            }
            if(!isReadOnlyMode.getValue()) {


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

    private void updateCell(String actionLine, String cellId) throws IOException {
        try {
            logic.updateCell(cellId, actionLine);
            updateSheet(logic.getSheet());
            CellDTO currCell = logic.getCell(new CoordinateImpl(cellId));
            cellClicked(currCell, coordToController.get(new CoordinateImpl(cellId)).getCell());
            actionLineController.addVersion();
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    public void updateCellClicked(TextField actionLine, TextField cellId) throws IOException {
        updateCell(actionLine.getText(), cellId.getText());
        actionLine.setText("");
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
        rangeAreaController.getAddRange().disableProperty().bind((isReadOnlyMode.or(isdeleteRangeMode)).and(isDynmicMode));
        rangeAreaController.getDeleteRange().disableProperty().bind((isReadOnlyMode.or(isdeleteRangeMode)).and(isDynmicMode));
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
        styleSheetController.getStyleSheet().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode));
    }

    public void initializeCommands(CommandsController commandsController) {
        commandArea.visibleProperty().bind(isLoaded);
        commandArea.disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode));
        commandsController.getCustomFormula().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode).or(currCell.isClicked.not()));

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
        actionLine.setDisable(true);
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
        return logic.getColumsItem(col, theRange);
    }

    public Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected) {
        return logic.getColumsItem(col, theRange, rowSelected);
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

    public void versionSelected(int version) throws IOException {
        SheetDTO sheetbyVersion = logic.getSheetbyVersion(version - 1);
        for (Coordinate coordinate : coordToController.keySet()) {
            backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
            coordToController.get(coordinate).restCellArtitube();
        }
        readOnlyCoord = new HashSet<>(coordToController.keySet());
        updateSheet(sheetbyVersion);
        modeReadOnly();
    }

    public void dynmicAnlyzeForCell() {
        turnOnDynmicAnlyzeMode();
        currCell.cellContoller.turnOnDynmicAnlayze();
    }

    private void turnOnDynmicAnlyzeMode() {
        modeReadOnly();
        isDynmicMode.setValue(true);
    }

    public void updateCellDynmicAnlyaze(double value, String cellId) {
        logic.updateDaynmicAnlayze(cellId, String.valueOf(value));
        updateSheet(logic.getSheet());
        if(lastDynmicCell.equals(cellId)){
            logic.deleteSheet();
        }
        else{
            countDynmicAnlyze++;
        }
        lastDynmicCell = cellId;
    }

    public void dynmicAnlyzeForSheet() {
        turnOnDynmicAnlyzeMode();
        for(Coordinate coordinate : coordToController.keySet()){
            if(coordToController.get(coordinate).isNaturalNumber()){
                coordToController.get(coordinate).turnOnDynmicAnlayze();
                daynmicCells.add(coordinate);
            }
        }
    }

    public List<String> getRanges() {
        return rangeAreaController.getRanges();
    }

    public List<EffectiveValue> getCustomRange(String range){
        RangeDTO tempRange = logic.createTempRange(range);
        List<EffectiveValue> effectiveValues = new ArrayList<>();
        List<CellDTO> cells = tempRange.getRangeCells();
        for(CellDTO cell : cells){
            effectiveValues.add(cell.getOriginalEffectiveValue());
        }
        return effectiveValues;
    }

    public List<EffectiveValue> getRange(String text) {
        RangeDTO range = logic.getRange(text);
        List<EffectiveValue> effectiveValues = new ArrayList<>();
        List<CellDTO> cells = range.getRangeCells();
        for(CellDTO cell : cells){
            effectiveValues.add(cell.getOriginalEffectiveValue());
        }
        return effectiveValues;
    }

    public void onLeftColumnDragged(double newWidth) {
        double newWithSheet = sheet.getPrefWidth() + newWidth;
        sheet.setPrefWidth(newWithSheet);
    }

    public void onBotomDragged(double deltaY) {
        double newHeightSheet = sheet.getPrefHeight() + deltaY;
        sheet.setPrefHeight(newHeightSheet);
    }

    public String predictCalculate(String expression) throws IOException, ClassNotFoundException {
        return logic.predictCalculate(expression);
    }

    public void setCellDynmic(int row, int col) {
        numberRowCell.get(row - 1).setSizeRowDynmic();
        numberColCell.get(col - 1).setSizeColDynmic();
    }

    public void restCellDynmic(int row, int col) {
        numberRowCell.get(row - 1).resetSizeRowDynmic();
        numberColCell.get(col - 1).resetSizeColDynmic();
    }

    public void addExpression(String expressionUi) throws IOException {
        updateCell(expressionUi, currCell.cellid.getValue());
    }
}

