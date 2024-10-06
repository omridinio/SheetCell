package Components.MangerSheet.PermissionsTable;

import Components.MangerSheet.ManggerSheetController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PermissionsTableController {

    @FXML
    private TableColumn<?, ?> permissionStatus;

    @FXML
    private TableColumn<?, ?> permissionType;

    @FXML
    private TableView<?> table;

    @FXML
    private TableColumn<?, ?> username;

    private SimpleDoubleProperty width;

    private ManggerSheetController manggerSheetController;

    public void initialize() {
//        width = (SimpleDoubleProperty) table.prefWidthProperty();
//        permissionStatus.prefWidthProperty().bind(width.divide(3));
//        permissionType.prefWidthProperty().bind(width.divide(3));
//        username.prefWidthProperty().bind(width.divide(3));
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }


}
