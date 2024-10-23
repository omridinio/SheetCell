package Components.MangerSheet.AvailableSheets;

import Components.MangerSheet.ManggerSheetController;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;


import java.util.*;

public class AvailableSheetsController {

    @FXML
    private TableColumn<SheetBasicData,String> sheetOwner;

    @FXML
    private TableColumn<SheetBasicData,String> sheetPermission;

    @FXML
    private TableColumn<SheetBasicData,String> sheetName;

    @FXML
    private TableColumn<SheetBasicData,String> sheetSize;

    @FXML
    private TableView<SheetBasicData> table;

    private SimpleDoubleProperty width;

    private ManggerSheetController manggerSheetController;

    private Map<String, SheetBasicData> sheets = new HashMap<>();

    private TimerTask sheetRefresher;

    private Timer timer;


    @FXML
    void slectedRow(MouseEvent event) {
        if(table.getSelectionModel().getSelectedItem() != null){
            manggerSheetController.setSelectedSheet(table.getSelectionModel().getSelectedItem());
        }
    }

    public void initialize() {
        sheetOwner.setCellValueFactory(new PropertyValueFactory<SheetBasicData,String>("sheetOwner"));
        sheetPermission.setCellValueFactory(new PropertyValueFactory<SheetBasicData,String>("sheetPermission"));
        sheetName.setCellValueFactory(new PropertyValueFactory<SheetBasicData,String>("sheetName"));
        sheetSize.setCellValueFactory(new PropertyValueFactory<SheetBasicData,String>("sheetSize"));
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

    private void updateSheets(List<SheetBasicData> sheets){;
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        ObservableList<TableColumn<SheetBasicData, ?>> sortedColumns = table.getSortOrder();
        Platform.runLater(() -> {
            if (!sortedColumns.isEmpty()) {
                TableColumn<SheetBasicData, ?> sortedColumn = sortedColumns.get(0);
                table.getSortOrder().clear();
                table.getSortOrder().add(sortedColumn);
                table.sort();
            }
            else {
                table.getItems().clear();
                table.getItems().addAll(sheets);
            }
            if(selectedIndex >= 0 && selectedIndex < table.getItems().size()){
                table.getSelectionModel().select(selectedIndex);
                if(table.getSelectionModel().getSelectedItem() != null){
                    manggerSheetController.setSelectedSheet(table.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void stratSheetRefresher(){
       sheetRefresher = new SheetRefresher(this::updateSheets, manggerSheetController.inScreenProperty());
       timer = new Timer();
       timer.schedule(sheetRefresher, 100, 500);
    }

    public void init() {
        stratSheetRefresher();
    }

    public void clearSheetSelct() {
        table.getSelectionModel().clearSelection();
    }

    public void close() {
        if(sheetRefresher != null && timer != null){
            sheetRefresher.cancel();
            timer.cancel();
        }
    }
}