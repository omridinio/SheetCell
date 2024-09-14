package Components.Commands.Filter.FilterColums;

import Components.Commands.Filter.FilterColums.ComboChoiceBox.ComboChoiceBoxController;
import Components.Commands.Filter.FilterController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Popup;

import java.io.IOException;
import java.util.*;

public class FilterColumsController {

    @FXML
    private ChoiceBox<Character> colums;

    @FXML
    private Button item;

    private FilterController filterController;

    private Popup popup = new Popup();

    private ComboChoiceBoxController ColscomboChoiceBoxControllers;

    public BooleanProperty columsSelectedProperty = new SimpleBooleanProperty(false);




    @FXML
    void itemClicked(ActionEvent event) throws IOException {
        if(popup.isShowing()) {
            popup.hide();
        }
        else {
            popup.show(item, item.localToScreen(0, item.getHeight()).getX(), item.localToScreen(0, item.getHeight()).getY());
        }
    }

    public void initialize() {
        colums.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Filter/FilterColums/ComboChoiceBox/comboChoiceBox.fxml"));
                Node comboChoiceBox = null;
                try {
                    comboChoiceBox = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ComboChoiceBoxController comboChoiceBoxController = loader.getController();
                ColscomboChoiceBoxControllers = comboChoiceBoxController;
                Map<Integer, String> itemInCol = filterController.getItemInColum(colums.getValue());
                Set<String> colItem = new HashSet<String>(itemInCol.values());
                comboChoiceBoxController.setFilterColumsController(this);
                comboChoiceBoxController.setColumIndex((Integer) (colums.getValue() +1 - 'A'));
                comboChoiceBoxController.setItemsInColums(itemInCol);
                for (String s : colItem) {
                    if(s != null) {
                        CheckBox checkBox = new CheckBox(s);
                        if(s.equals("")){
                            checkBox.setText("Empty");
                        }
                        else
                            checkBox.setText(s);
                        comboChoiceBoxController.addCheckBox(checkBox);
                    }
                }
                popup.setAutoHide(true); // Automatically hide when clicking outside
                popup.getContent().clear();
                popup.getContent().add(comboChoiceBox);
                filterController.addLevelOn();
                filterController.ableOk();
            }
        });
        columsSelectedProperty.bind(colums.getSelectionModel().selectedItemProperty().isNotNull());

    }

    public void setColums(ChoiceBox<Character> colums) {
        for (Character c : colums.getItems()) {
            this.colums.getItems().add(c);
        }
    }

    public void setFilterController(FilterController filterController) {
        this.filterController = filterController;
    }

    public int getFirstRowInRane(){
        return filterController.getFirstRowInRane();
    }

    public List<Integer> getRowSelcted() {
        return ColscomboChoiceBoxControllers.rowSelcted();
    }

    public void turnOf(){
        colums.setDisable(true);
        item.setDisable(true);
    }

    public void turnOn(){
        colums.setDisable(false);
        item.setDisable(false);
    }

    public int getColsIndex(){
        return (Integer) (colums.getValue() + 1 - 'A');
    }

    public char getColumsValue(){
        return colums.getValue();
    }
}
