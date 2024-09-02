package Components.Shitcell;

import Properties.CellUI;
import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Components.Cell.CellContoller;

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

    private CellUI currCell;

    //my dataMember
    Map<Coordinate,CellContoller> coordToController = new HashMap<>();

    private Logic logic = new ImplLogic();

    public ShitsellController() {
        currCell = new CellUI();
    }



    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
        cellId.textProperty().bind(currCell.cellid);
        originalValue.textProperty().bind(currCell.originalValue);
        lastVersion.textProperty().bind(currCell.lastVersion);
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        restSheet();
        logic.creatNewSheet(file.getAbsolutePath());
        int row = logic.getSheet().getRowCount();
        int col = logic.getSheet().getColumnCount();
        createEmptySheet(col, row);
        updateSheet(logic.getSheet());
        //(logic.getSheet());
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
    private void createEmptySheet(int col, int row) throws IOException {
        sheet.getRowConstraints().clear();
        sheet.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        for(int i = 0; i <= row; i++){
            for(int j = 0; j <= col; j++){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/cell.fxml"));
                Node cell = loader.load();
                CellContoller cellContoller = loader.getController();
                cellContoller.setShitsellController(this);
                GridPane.setRowIndex(cell, i);
                GridPane.setColumnIndex(cell, j);
                if(i == 0 && j == 0){
                    rowConstraints.setPrefHeight(cellContoller.getHeight());
                    columnConstraints.setPrefWidth(cellContoller.getWidth());
                    cell.getStyleClass().add("empty");
                }
                else if(i == 0){
                    columnConstraints.setPrefWidth(cellContoller.getWidth());
                    cellContoller.setText(String.valueOf((char)('A' + j - 1)));
                    cell.getStyleClass().add("ABC");
                }
                else if(j == 0){
                    rowConstraints.setPrefHeight(cellContoller.getHeight());
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

    public void cellClicked(CellDTO cell) {
        currCell.cellid.setValue(cell.getId());
        currCell.originalValue.setValue(cell.getOriginalValue());
        currCell.lastVersion.setValue(String.valueOf(cell.getLastVersionUpdate()));
    }
}

