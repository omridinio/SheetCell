package Components.Shitcell;

import Components.ActionLine.ActionLineController;
import Components.Commands.CommandsController;
import Components.Error.ErrorController;
import Components.MangerSheet.ManggerSheetController;
import Components.RangeArea.RangeAreaController;
import Components.StyleSheet.StyleSheetController;
import dto.impl.CoordinateAdapter;
import dto.impl.PermissionType;
import Properties.CellUI;
import dto.impl.Coordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import dto.impl.RangeDTO;

import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import Components.Cell.CellContoller;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import static java.lang.Thread.sleep;


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
    private AnchorPane allTheSheet;

    @FXML
    private Button chat;

    @FXML
    private Label hello;




    //my dataMember
    private CellUI currCell;
    private BooleanProperty isReadOnlyMode = new SimpleBooleanProperty(false);
    private BooleanProperty isdeleteRangeMode = new SimpleBooleanProperty(false);
    private BooleanProperty isDynmicMode = new SimpleBooleanProperty(false);
    Map<Coordinate,CellContoller> coordToController = new HashMap<>();
    List<CellContoller> numberColCell = new ArrayList<>();
    List<CellContoller> numberRowCell = new ArrayList<>();
    Map<Coordinate,CellContoller> backupCoordToController = new HashMap<>();
    private SheetDTO currSheet;
    private BooleanProperty isLoaded = new SimpleBooleanProperty(false);
    private Button currRange;
    private List<CellContoller> cellsDependOnThem = new ArrayList<>();
    private List<CellContoller> getCellsDependOnhim = new ArrayList<>();
    private List<CellContoller> cellChoosed = new ArrayList<>();
    private Set<Coordinate> readOnlyCoord = new HashSet<>();
    private List<Coordinate> daynmicCells = new ArrayList<>();
    private ManggerSheetController manggerSheetController;
    private SimpleBooleanProperty isReadMode = new SimpleBooleanProperty();
    private SimpleBooleanProperty isReaderPermission = new SimpleBooleanProperty();
    private TimerTask sheetRefresher;
    private Timer timer;




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
        isReadMode.bind(isReaderPermission);
    }

    public GridPane getSheet() {
        return sheet;
    }

    public void showSheet(SheetDTO sheet, PermissionType permissionType) {
        try {
            hello.textProperty().bind(manggerSheetController.getHelloProperty());
            currSheet = sheet;
            if(!isLoaded.get()){
                restSheet();
                isLoaded.setValue(true);
                int row = sheet.getRowCount();
                int col = sheet.getColumnCount();
                int width = sheet.getWidth();
                int height = sheet.getThickness();
                createEmptySheet(col, row, width, height);
                updateSheet(sheet);
                createRanges();
                actionLineController.addNewVersions(sheet.getVersion(), false, true);
                actionLine.setDisable(false);
                if(permissionType == PermissionType.READER){
                    isReaderPermission.setValue(true);
                }
                stratSheetRefresher();
            }

        } catch (Exception e){
            ErrorController.showError(e.getMessage());
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
        actionLine.setDisable(false);
        //actionLineController.getVersionSelctor().setValue(actionLineController.getVersionSelctor().getItems().getLast());
        actionLineController.getVersionSelctor().setValue(currSheet.getVersion());
        String finalUrl = HttpUrl
                .parse(Constants.DELETE_DYNAMIC_SHEET)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    ErrorController.showError(response.body().string());
                }
                response.body().string();
            }
        });
        if(isDynmicMode.getValue()){
            for(Coordinate coordinate : daynmicCells){
                coordToController.get(coordinate).turnOffDynmicAnlayze();
            }
            daynmicCells.clear();
            if(currCell.cellContoller != null){
                currCell.cellContoller.turnOffDynmicAnlayze();
            }
            isDynmicMode.setValue(false);
            updateSheet(currSheet);
        }
    }

    @FXML
    private void backToDashboardClicked() {
        String finalUrl = HttpUrl
                .parse(Constants.RESET_SESSION)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    ErrorController.showError(response.body().string());
                }
                response.body().string();
            }
        });
        if(sheetRefresher != null){
            sheetRefresher.cancel();
            timer.cancel();
        }
        manggerSheetController.switchManagerSheet();
    }

    @FXML
    void chatClicked(){
        manggerSheetController.chatClick(chat);
    }

    private void stratSheetRefresher(){
        sheetRefresher = new SheetVersionRefresher(currSheet.getVersion(), this::updateVersion, currSheet.getSheetName());
        timer = new Timer();
        timer.schedule(sheetRefresher, 100, 500);
    }

    private void updateVersion(int version) {
        try {
            actionLineController.addNewVersions(version, isReaderPermission.getValue(),false);
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }


    private void createRanges() throws IOException {
        rangeAreaController.restRangeArea();
        List<String> ranges = getRangeName();
        if (ranges == null) {
            return;
        }
        insertRangesToSheet(ranges);
    }

    public void insertRangesToSheet(List<String> ranges){
        for (String range : ranges) {
            getRange(range, (rangeId, rangeDTO) -> {
                Platform.runLater(() -> {
                    try {
                        rangeAreaController.addRange(rangeId, rangeDTO);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
        }
    }


    private void getRange(String rangeId, BiConsumer<String,RangeDTO> callback) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_RANGE)
                .newBuilder()
                .addQueryParameter("rangeId", rangeId)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    System.out.println("Error: " + response.code());
                } else {
                    String jsonArrayOfSheetNames = response.body().string();
                    RangeDTO range = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, RangeDTO.class);
                    Platform.runLater(() -> callback.accept(rangeId, range));
                }
            }
        });
    }

    private  List<String> getRangeName() throws IOException {
       String finalUrl = HttpUrl
                .parse(Constants.GET_RANGES_NAME)
                .newBuilder()
                .build()
                .toString();
        Response response = HttpClientUtil.runSync(finalUrl);
        if (response.code() != 200) {
            ErrorController.showError(response.body().string());
        } else {
            String jsonArrayOfSheetNames = response.body().string();
            String[] rangesName = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, String[].class);
            List<String> ranges = List.of(rangesName);
            return ranges;
        }
        return null;
    }

    private void updateSheet(SheetDTO sheet){
        for (int i = 1; i <= sheet.getRowCount(); i++) {
            for (int j = 1; j <= sheet.getColumnCount(); j++) {
                Coordinate coordinate = new Coordinate(i, j);
                coordToController.get(coordinate).setCellDTO(sheet.getCell(coordinate));
            }
        }
    }

    private void createEmptySheet(int col, int row, int widthCell, int heightCell) throws IOException {
        sheet.getRowConstraints().clear();
        sheet.getColumnConstraints().clear();
        sheet.setPrefHeight((row+1) * heightCell);
        sheet.setPrefWidth((col+1) * widthCell);
        for(int i = 0; i <= row; i++){
            for(int j = 0; j <= col; j++){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/cell.fxml"));
                Node cell = loader.load();
                CellContoller cellContoller = loader.getController();
                cellContoller.setShitsellController(this);
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
                    cellContoller.setBackgroundColor("E0E0E0");
                }
                else if(i == 0){
                    cellContoller.setIsTitle(true);
                    ColumnConstraints columnConstraints = new ColumnConstraints();
                    cellContoller.setColumnConstraints(columnConstraints);
                    columnConstraints.setPrefWidth(widthCell);
                    cellContoller.setText(String.valueOf((char)('A' + j - 1)));
                    cell.getStyleClass().add("ABC");
                    cellContoller.ableColSpearte();
                    sheet.getColumnConstraints().add(columnConstraints);
                    cellContoller.setBackgroundColor("E0E0E0");
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
                    sheet.getRowConstraints().add(rowConstraints);
                    cellContoller.setBackgroundColor("E0E0E0");
                    numberRowCell.add(cellContoller);
                }
                else{
                    cell.getStyleClass().add("cell");
                    cell.setId(String.valueOf((char)('A' + j - 1)) + i);
                    Coordinate coordinate = new Coordinate(i, j);
                    coordToController.put(coordinate, cellContoller);
                }
                cell.prefWidth(widthCell);
                cell.prefHeight(heightCell);
                sheet.add(cell, j, i);
            }
        }
    }

    private void restSheet(){
        if(sheet != null)
            sheet.getChildren().clear();
        currCell.isClicked.setValue(false);
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
            for (int i = 1; i <= currSheet.getRowCount(); i++) {
                String cellId = col + i;
                Coordinate coordinate = new Coordinate(cellId);
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
            currCell.lastUserUpdate.setValue(cell.getLastUserUpdate());
            currCell.clickedCell = button;
            currCell.isClicked.setValue(true);
            currCell.clickedCell.getStyleClass().add("clicked");
            currCell.cellContoller = coordToController.get(new Coordinate(cell.getId()));
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
            Coordinate coordinate = new Coordinate(id);
            try {
                //CellDTO cell = logic.getCell(coordinate);
                CellDTO cell = currSheet.getCell(coordinate);
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
            if(!actionLineController.isUpdate.getValue()){
                ErrorController.showError("Error: A more recent version of the sheet is available");
            }
            else {
                updateCellInSheet(actionLine, cellId, this::updateSheet);
                CellDTO currCell = currSheet.getCell(new Coordinate(cellId));
                cellClicked(currCell, coordToController.get(new Coordinate(cellId)).getCell());
                actionLineController.addVersion();
            }
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    private void updateCellInSheet(String newValue, String cellId, Consumer<SheetDTO> updateSheet){
        try {
            String finalUrl = HttpUrl
                    .parse(Constants.UPDATE_CELL)
                    .newBuilder()
                    .addQueryParameter("cellId", cellId)
                    .addQueryParameter("newValue", newValue)
                    .build()
                    .toString();
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() == 200) {
                String jsonSheetName = response.body().string();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                SheetDTO newSheet = gson.fromJson(jsonSheetName, ImplSheetDTO.class);
                currSheet = newSheet;
                updateSheet.accept(currSheet);
            }
            else {
                ErrorController.showError(response.body().string());
            }
            Platform.runLater(() -> {
                try {
                    createRanges();
                } catch (IOException e) {
                    ErrorController.showError(e.getMessage());
                }
            });
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }


    public void updateCellClicked(TextField actionLine, TextField cellId) throws IOException {
        updateCell(actionLine.getText(), cellId.getText());
        actionLine.setText("");
    }

    public void setRange(String rangeId, String theRange)  {
        try{
            addNewRange(rangeId, theRange);
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    private void addNewRange(String rangeId, String theRange) {
        try {
            String finalUrl = HttpUrl
                    .parse(Constants.ADD_NEW_RANGE)
                    .newBuilder()
                    .addQueryParameter("rangeId", rangeId)
                    .addQueryParameter("theRange", theRange)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        ErrorController.showError(response.body().string());
                    } else {
                        String jsonRange = response.body().string();
                        RangeDTO range = Constants.GSON_INSTANCE.fromJson(jsonRange, RangeDTO.class);
                    }
                    Platform.runLater(() -> {
                        try {
                            createRanges();
                        } catch (IOException e) {
                            ErrorController.showError(e.getMessage());
                        }
                    });
                }
            });
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
    }

    public void rangeClicked(RangeDTO rangeDTO, Button range, FlowPane rangeArea) {
        clearCellsMark();
        clearCellChoosed();
        rangeArea.requestFocus();
        List<CellDTO> cells = rangeDTO.getRangeCells();
        for (CellDTO cell : cells) {
            coordToController.get(new Coordinate(cell.getId())).getCell().getStyleClass().add("dependThem");;
            coordToController.get(new Coordinate(cell.getId())).setDependOnThem();
            cellsDependOnThem.add(coordToController.get(new Coordinate(cell.getId())));
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
        actionLineController.getUserName().textProperty().bind(currCell.lastUserUpdate);
        actionLineController.getActionLine().disableProperty().bind(currCell.isClicked.not().or(isReadOnlyMode).or(isReadMode));
        actionLineController.getActionLine().editableProperty().bind(currCell.isClicked);
        actionLineController.getCellId().disableProperty().bind(isLoaded.not());
        actionLineController.getCellId().editableProperty().bind(isLoaded);
        actionLineController.getUserName().disableProperty().bind(isLoaded.not());
        actionLineController.getUserName().editableProperty().bind(isLoaded);
        actionLineController.getLastVersion().disableProperty().bind(isLoaded.not());
        actionLineController.getOriginalValue().disableProperty().bind(isLoaded.not());
        actionLineController.getUpdateValue().disableProperty().bind(isReadOnlyMode.or(isReadMode));
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
        rangeAreaController.getAddRange().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode).or(isReadMode));
        rangeAreaController.getDeleteRange().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode).or(isReadMode));
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
                cell.setFontColor(value.toString().substring(2));

            }
        }
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
        styleSheetController.getRestCell().disableProperty().bind(currCell.isClicked.not());
    }

    public void initializeCommands(CommandsController commandsController) {
        commandArea.visibleProperty().bind(isLoaded);
        commandArea.disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode));
        commandsController.getCustomFormula().disableProperty().bind(isReadOnlyMode.or(isdeleteRangeMode).or(isDynmicMode).or(currCell.isClicked.not()).or(isReadMode));

    }

    public void sortRangeClicked(String range, List<Integer> dominantCols) throws IOException, ClassNotFoundException {
        //Map<Coordinate, CellDTO> sortRange = logic.getSortRange(range, dominantCols);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(dominantCols);
        String version = String.valueOf(currSheet.getVersion());
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Constants.SORT)
                .post(body)
                .addHeader("range", range)
                .build();
        Response response = HttpClientUtil.runSync(request);
        if (response.code() != 200) {
            ErrorController.showError(response.body().string());
        } else {
            String jsonResponse = response.body().string();
            Gson gsonFromServer = new GsonBuilder()
                    .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                    .create();
            Type mapType = new TypeToken<Map<Coordinate, CellDTO>>(){}.getType();
            Map<Coordinate, CellDTO> sortedSheet = gsonFromServer.fromJson(jsonResponse, mapType);
            createReadOnlySheet(sortedSheet);
            updateCells();
            readOnlyCoord = sortedSheet.keySet();
            modeReadOnly();
        }
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

    public List<Integer> getColsInRange(String range) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_THE_RANGE_OF_THE_RANGE)
                .newBuilder()
                .addQueryParameter("sheetName", currSheet.getSheetName())
                .addQueryParameter("range", range)
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonArrayOfSheetNames = response.body().string();
                int[] cols = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, int[].class);
                return Arrays.stream(cols).boxed().collect(Collectors.toList());
            }
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
        return null;
    }

    private void createReadOnlySheet(Map<Coordinate, CellDTO>sortRange) throws IOException, ClassNotFoundException {
        for (Coordinate coordinate : coordToController.keySet()){
            backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
        }
        for (Coordinate coordinate : sortRange.keySet()){
            switchCells(coordToController.get(coordinate), backupCoordToController.get((new Coordinate(sortRange.get(coordinate).getId()))));
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
        try {
            String finalUrl = HttpUrl
                    .parse(Constants.DELETE_RANGE)
                    .newBuilder()
                    .addQueryParameter("rangeId", rangeId)
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                        ErrorController.showError(response.body().string());
                    } else {
                        response.body().string();
                        Platform.runLater(() -> {
                            try {
                                createRanges();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }
                }
            });
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }

    }

    public void deleteRangeModeOff() {
        isdeleteRangeMode.setValue(false);
    }

    public Map<Integer, String> getColumsItem(int col, String theRange){
        String version = String.valueOf(currSheet.getVersion());
        Request request = new Request.Builder()
                .url(Constants.GET_COL_ITEMS)
                .post(RequestBody.create(null, new byte[0]))
                .addHeader("sheetName", currSheet.getSheetName())
                .addHeader("range", theRange)
                .addHeader("version", version)
                .addHeader("col", String.valueOf(col))
                .build();
        try {
            Response response = HttpClientUtil.runSync(request);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonArrayOfSheetNames = response.body().string();
                Type mapType = new TypeToken<Map<Integer, String>>(){}.getType();
                return Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, mapType);
            }
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
        return null;
    }

    public Map<Integer, String> getColumsItem(int col, String theRange, List<Integer> rowSelected) {
        Gson gson = new Gson();
        String jsonBody = gson.toJson(rowSelected);
        String version = String.valueOf(currSheet.getVersion());
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(Constants.GET_COL_ITEMS)
                .post(body)
                .addHeader("sheetName", currSheet.getSheetName())
                .addHeader("range", theRange)
                .addHeader("version", version)
                .addHeader("col", String.valueOf(col))
                .build();
        try {
            Response response = HttpClientUtil.runSync(request);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonArrayOfSheetNames = response.body().string();
                Type mapType = new TypeToken<Map<Integer, String>>(){}.getType();
                return Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, mapType);
            }
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
        return null;
    }

    public void filterOkClicked(List<Integer> rowSelected, String theRange) throws IOException, ClassNotFoundException {
        int firstRowInRange = Integer.parseInt(theRange.substring(1, theRange.indexOf('.')));
        int firstColInRange = theRange.charAt(0) - 'A' + 1;
        int lastColInRange = theRange.charAt(theRange.length() - 2) - 'A' + 1;
        String finalUrl = HttpUrl
                .parse(Constants.FILTER)
                .newBuilder()
                .addQueryParameter("range", theRange)
                .build()
                .toString();
        Response response = HttpClientUtil.runSync(finalUrl);
        if (response.code() != 200) {
            ErrorController.showError(response.body().string());
        } else {
            String jsonResponse = response.body().string();
            Gson gsonFromServer = new GsonBuilder()
                    .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                    .create();
            Type listType = new TypeToken<List<Coordinate>>(){}.getType();
            List<Coordinate> coordinatesOfRange = gsonFromServer.fromJson(jsonResponse, listType);
            readOnlyCoord = new HashSet<>(coordinatesOfRange);
            rowSelected.sort(Comparator.naturalOrder());
            for (Coordinate coordinate : coordToController.keySet()) {
                backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
            }
            int currRow = firstRowInRange;
            for (int row : rowSelected) {
                for (int col = firstColInRange; col <= lastColInRange; col++) {
                    Coordinate prevCoordinate = new Coordinate(row, col);
                    Coordinate newCoordinate = new Coordinate(currRow, col);
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

    public void versionSelected(int version) throws IOException {
        String finalUrl = HttpUrl
                .parse(Constants.SHEET_BY_VERSION)
                .newBuilder()
                .addQueryParameter("version", String.valueOf(version - 1))
                .build()
                .toString();
        Response response = HttpClientUtil.runSync(finalUrl);
        if (response.code() != 200) {
            ErrorController.showError(response.body().string());
        } else {
            String jsonSheetName = response.body().string();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                    .create();
            SheetDTO sheetbyVersion = gson.fromJson(jsonSheetName, ImplSheetDTO.class);
            for (Coordinate coordinate : coordToController.keySet()) {
                backupCoordToController.put(coordinate, coordToController.get(coordinate).duplicate());
                coordToController.get(coordinate).restCellArtitube();
            }
            readOnlyCoord = new HashSet<>(coordToController.keySet());
            updateSheet(sheetbyVersion);
            modeReadOnly();
        }
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
        String finalUrl = HttpUrl
                .parse(Constants.DYNMIC_ANLYZE)
                .newBuilder()
                .addQueryParameter("value", String.valueOf(value))
                .addQueryParameter("cellId", cellId)
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonSheetName = response.body().string();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                SheetDTO dynamicSheet =gson.fromJson(jsonSheetName, ImplSheetDTO.class);
                updateSheet(dynamicSheet);
            }
        } catch (Exception e) {
            ErrorController.showError(e.getMessage());
        }
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

    public List<String> getCustomRange(String range){
        String finalUrl = HttpUrl
                .parse(Constants.GET_TEMP_RANGE)
                .newBuilder()
                .addQueryParameter("sheetName", currSheet.getSheetName())
                .addQueryParameter("theRange", range)
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonArrayOfSheetNames = response.body().string();
                RangeDTO tempRange = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, RangeDTO.class);
                List<String> effectiveValues = new ArrayList<>();
                List<CellDTO> cells = tempRange.getRangeCells();
                for(CellDTO cell : cells){
                    effectiveValues.add(cell.getOriginalEffectiveValue());
                }
                return effectiveValues;
            }

        } catch (IOException e) {
            ErrorController.showError(e.getMessage());
        }
        return null;
    }

    public List<String> getRanges(String text) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_RANGE)
                .newBuilder()
                .addQueryParameter("rangeId", text)
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonArrayOfSheetNames = response.body().string();
                RangeDTO range = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, RangeDTO.class);
                List<String> effectiveValues = new ArrayList<>();
                List<CellDTO> cells = range.getRangeCells();
                for(CellDTO cell : cells){
                    effectiveValues.add(cell.getOriginalEffectiveValue());
                }
                return effectiveValues;
            }
        } catch (IOException e) {
            ErrorController.showError(e.getMessage());
        }
        return null;
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
        String finalUrl = HttpUrl
                .parse(Constants.PREDICT_CALCULATE)
                .newBuilder()
                .addQueryParameter("cellId", currCell.cellid.getValue())
                .addQueryParameter("expression", expression)
                .build()
                .toString();
        Response response = HttpClientUtil.runSync(finalUrl);
        return response.body().string();
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

    public void restCellClicked() {
        currCell.cellContoller.restCellArtitube();
    }

    public void restSheetClicked() {
        for (Coordinate coordinate : coordToController.keySet()) {
            coordToController.get(coordinate).restCellArtitube();
        }
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController){
        this.manggerSheetController = manggerSheetController;
    }

    public String getSheetName() {
        return currSheet.getSheetName();
    }

    public void getUpdateSheetVersion(int updateVersion) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_UPDATE_SHEET_VERSION)
                .newBuilder()
                //.addQueryParameter("sheetName", currSheet.getSheetName())
                .addQueryParameter("newVersion", String.valueOf(updateVersion - 1))
                .build()
                .toString();
        try {
            Response response = HttpClientUtil.runSync(finalUrl);
            if (response.code() != 200) {
                ErrorController.showError(response.body().string());
            } else {
                String jsonSheetName = response.body().string();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                SheetDTO newSheet =gson.fromJson(jsonSheetName, ImplSheetDTO.class);
                currSheet = newSheet;
                updateSheet(currSheet);
                actionLineController.removeEffect();
            }
        } catch (IOException e) {
            ErrorController.showError(e.getMessage());
        }
    }

    public void close() {
        if(sheetRefresher != null && timer != null){
            sheetRefresher.cancel();
            timer.cancel();
        }
    }
}

