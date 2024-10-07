package Components.MangerSheet.PermissionsTable;

import Components.MangerSheet.ManggerSheetController;
import dto.impl.PermissionRequest;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class PermissionsTableController {

    @FXML
    private TableColumn<PermissionTable, String> permissionStatus;

    @FXML
    private TableColumn<PermissionTable, String> permissionType;

    @FXML
    private TableView<PermissionTable> table;

    @FXML
    private TableColumn<PermissionTable, String> username;

    private SimpleDoubleProperty width;

    private ManggerSheetController manggerSheetController;

    private TimerTask permissionRefresher;

    private Timer timer;



    public void initialize() {
        permissionStatus.setCellValueFactory(new PropertyValueFactory <PermissionTable, String>("permissionStatus"));
        permissionType.setCellValueFactory(new PropertyValueFactory <PermissionTable, String>("permissionType"));
        username.setCellValueFactory(new PropertyValueFactory <PermissionTable, String>("username"));
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

    private void updateTable(List<PermissionRequest> permissionRequests) {
        List<PermissionTable> permissionTables = permissionRequests.stream()
                .map(PermissionTable::new)
                .collect(Collectors.toList());
        Platform.runLater(() -> {
            table.getItems().clear();
            table.getItems().addAll(permissionTables);
        });
    }

    private void startRefreshTable() {
        permissionRefresher = new PermissionRefresh(this::updateTable, manggerSheetController.isSheetSelectedProperty(), manggerSheetController.getSheetNameProperty());
        timer = new Timer();
        timer.schedule(permissionRefresher, 100, 500);
    }


    public void init() {
        startRefreshTable();
    }


}
