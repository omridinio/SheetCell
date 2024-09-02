import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import dto.SheetDTO;
import expression.api.EffectiveValue;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import body.Logic;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    //my dataMember
    //Map<Coordinate,Button> sheets = new HashMap<>();
    private Logic logic = new ImplLogic();


    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
//
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        if(sheet != null)
            sheet.getChildren().clear();
        //sheets.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        logic.creatNewSheet(file.getAbsolutePath());
        int row = logic.getSheet().getRowCount();
        int col = logic.getSheet().getColumnCount();
        addDynamicButtons(col, row);
        //(logic.getSheet());
    }

    private void showSheet(SheetDTO sheet){
        for (int i = 1; i <= sheet.getRowCount(); i++) {
            for (int j = 1; j <= sheet.getColumnCount(); j++) {
                Coordinate coordinate = new CoordinateImpl(i, j);
                EffectiveValue effectiveValue = sheet.getEfectivevalueCell(coordinate);
                if(effectiveValue != null) {
                    //sheets.get(coordinate).setText(effectiveValue.toString());
                }
            }
        }
    }



    // Method to dynamically add buttons
    private void addDynamicButtons(int col, int row) throws IOException {
        sheet.getRowConstraints().clear();
        sheet.getColumnConstraints().clear();
        RowConstraints rowConstraints = new RowConstraints();
        ColumnConstraints columnConstraints = new ColumnConstraints();
        for(int i = 0; i <= row; i++){
            for(int j = 0; j <= col; j++){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Cell/Cell.fxml"));
                Node cell = loader.load();
                CellContoller cellContoller = loader.getController();
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
                    Coordinate coordinate = new CoordinateImpl(i, j);
                }
                sheet.add(cell, j, i);
                //sheet.getChildren().add(cell);
            }
        }

    }

    private void setupGridPane(int rows, int columns) {
        // Clear existing rows and columns


        // Add row constraints
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setMinHeight(30); // Set min height for row
            rowConstraints.setPrefHeight(30); // Set preferred height for row
            rowConstraints.setVgrow(Priority.ALWAYS); // Allow row to grow
            sheet.getRowConstraints().add(rowConstraints);
        }

        // Add column constraints
        for (int i = 0; i < columns; i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            columnConstraints.setMinWidth(50); // Set min width for column
            columnConstraints.setPrefWidth(50); // Set preferred width for column
            columnConstraints.setHgrow(Priority.ALWAYS); // Allow column to grow
            sheet.getColumnConstraints().add(columnConstraints);
        }
    }



}

