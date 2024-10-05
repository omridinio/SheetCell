package Components.MangerSheet.AvailableSheets;

import Components.MangerSheet.ManggerSheetController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class AvailableSheetsController {

    @FXML
    private TableColumn<?, ?> owner;

    @FXML
    private TableColumn<?, ?> premission;

    @FXML
    private TableColumn<?, ?> sheetName;

    @FXML
    private TableColumn<?, ?> sheetSize;

    @FXML
    private TableView<?> table;

    private SimpleDoubleProperty width;

    private ManggerSheetController manggerSheetController;


    public void initialize() {
//        width = (SimpleDoubleProperty) table.prefWidthProperty();
//        owner.prefWidthProperty().bind(width.divide(4));
//        premission.prefWidthProperty().bind(width.divide(4));
//        sheetName.prefWidthProperty().bind(width.divide(4));
//        sheetSize.prefWidthProperty().bind(width.divide(4));
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

}
