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
    private HBox charHbox;
    @FXML
    private VBox digitVbox;
    @FXML
    private GridPane tableGridPane;
    Map<Coordinate,Button> sheets = new HashMap<>();
    private Logic logic = new ImplLogic();


    // Initialize method will be called automatically after FXML is loaded
    @FXML
    public void initialize() {
//
    }

    @FXML
    private void loadFile(ActionEvent event) throws IOException, ClassNotFoundException, JAXBException {
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
        for (int i = 0; i < col; i++) {
            char ch = (char) ('A' + i);
            Button button = new Button(String.valueOf(ch));
            button.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(button, Priority.ALWAYS);
            charHbox.getChildren().add(button);
        }
        for (int i = 1; i <= row; i++) {
            String s = i < 10 ? "0" + i : String.valueOf(i);
            Button button = new Button(s);
            button.setMaxHeight(Double.MAX_VALUE);
            VBox.setVgrow(button, Priority.ALWAYS);
            digitVbox.getChildren().add(button);
        }
        GridPane innerGridPane = new GridPane();
        innerGridPane.setId("innerGridPane");
        innerGridPane.setPrefSize(500, 500); // Set preferred size for innerGridPane
        GridPane.setConstraints(innerGridPane, 1, 1);
        tableGridPane.getChildren().add(innerGridPane);

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                String s = String.valueOf((char) ('A' + i)) + (j + 1);
                Button button = new Button();
                sheets.put(new CoordinateImpl(s), button);
                button.setId(s);
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                GridPane.setHgrow(button, Priority.ALWAYS);
                GridPane.setVgrow(button, Priority.ALWAYS);
                innerGridPane.add(button, i, j);
            }
        }

    }
}

