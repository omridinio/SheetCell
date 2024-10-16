package Components.MangerSheet.AvailableSheets;

import Components.MangerSheet.ManggerSheetController;
import dto.impl.SheetBasicData;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
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
        manggerSheetController.setSelectedSheet(table.getSelectionModel().getSelectedItem());

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
//        for(SheetBasicData sheet : sheets){
//            this.sheets.put(sheet.getSheetName(), sheet);
//            Platform.runLater(() -> {
//                table.getItems().add(sheet);
//            });
//        }
        int selectedIndex = table.getSelectionModel().getSelectedIndex();
        Platform.runLater(() -> {
            table.getItems().clear();


            table.getItems().addAll(sheets);
            if(selectedIndex >= 0 && selectedIndex < table.getItems().size()){
                table.getSelectionModel().select(selectedIndex);
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
}