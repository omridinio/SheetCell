import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import dto.SheetDTO;
import expression.api.EffectiveValue;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import body.Logic;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShitsellController {
    @FXML
    private VBox tableVbox;
    Map<Coordinate,Button> sheets = new HashMap<>();
    private Logic logic = new ImplLogic();


    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
//
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
        tableVbox.getChildren().clear();
        sheets.clear();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File file = fileChooser.showOpenDialog(null);
        logic.creatNewSheet(file.getAbsolutePath());
        int row = logic.getSheet().getRowCount();
        int col = logic.getSheet().getColumnCount();
        addDynamicButtons(col, row);
        showSheet(logic.getSheet());
    }

    private void showSheet(SheetDTO sheet){
        for (int i = 1; i <= sheet.getRowCount(); i++) {
            for (int j = 1; j <= sheet.getColumnCount(); j++) {
                Coordinate coordinate = new CoordinateImpl(i, j);
                EffectiveValue effectiveValue = sheet.getEfectivevalueCell(coordinate);
                if(effectiveValue != null) {
                    sheets.get(coordinate).setText(effectiveValue.toString());
                }
            }
        }
    }



    // Method to dynamically add buttons
    private void addDynamicButtons(int col, int row) {
        for(int i = 0; i <= row; i++){
            HBox currVbox = new HBox();
            VBox.setVgrow(currVbox, Priority.ALWAYS);
            for(int j = 0; j <= col; j++){
                String s = "";
                Button button = new Button();
                button.setPrefHeight(50);
                button.setPrefWidth(50);
                button.setMaxWidth(Double.MAX_VALUE); // Make button fill available space
                button.setMaxHeight(Double.MAX_VALUE);
                HBox.setHgrow(button, Priority.ALWAYS); // Make the button grow within the HBox
                currVbox.getChildren().add(button); // Add button to the HBox
                button.getStyleClass().add(("row" + i));
                button.getStyleClass().add(("col" + j));
                if(i == 0 && j == 0){
                    button.getStyleClass().add("empty");
                    button.setId("empty");
                }
                else if(i == 0){
                    s = String.valueOf((char)('A' + j - 1));
                    button.setText(s);
                    button.getStyleClass().add("ABC");
                }
                else if(j == 0){
                    button.getStyleClass().add("digitVbox");
                    s = i < 10 ? "0" + i : String.valueOf(i);
                    button.setText(s);
                }
                else{
                    button.getStyleClass().add("cell");
                    Coordinate coordinate = new CoordinateImpl(i, j);
                    sheets.put(coordinate, button);
                }
            }
            tableVbox.getChildren().add(currVbox);
        }

    }
}

