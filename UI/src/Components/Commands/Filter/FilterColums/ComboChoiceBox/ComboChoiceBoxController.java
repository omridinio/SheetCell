package Components.Commands.Filter.FilterColums.ComboChoiceBox;

import Components.Commands.Filter.FilterColums.FilterColumsController;
import body.Coordinate;
import body.impl.CoordinateImpl;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class ComboChoiceBoxController {

    @FXML
    private VBox item;

    private Map<String, CheckBox> checkBoxesItem = new HashMap<>();

    private int columIndex;

    private Map<Integer, String> itemsInColums = new HashMap<>();

    private FilterColumsController filterColumsController;


    public void initialize() {

    }



    public void addCheckBox(CheckBox checkBox) {
        checkBox.setSelected(true);
        item.getChildren().add(checkBox);
        checkBoxesItem.put(checkBox.getText(), checkBox);
    }

    public void setFilterColumsController(FilterColumsController filterColumsController) {
        this.filterColumsController = filterColumsController;
    }

    public List<Integer> rowSelcted() {
        List<Integer> rowSelcted = new ArrayList<>();
        Set<String> selectedItems = getSelectedItems();
        for (int i : itemsInColums.keySet()) {
            if (selectedItems.contains(itemsInColums.get(i))) {
                rowSelcted.add(i);
            }
        }
        return rowSelcted;
    }

    public Map<Coordinate, Coordinate> getSelectedItemsPrevAndNewCoordinates() {
        Map<Coordinate, Coordinate> selectedItemsPrevAndNewCoordinates = new HashMap<>();
        Set<String> selectedItems = getSelectedItems();
        int stratIndex = filterColumsController.getFirstRowInRane();
        for (int i : itemsInColums.keySet()) {
            Coordinate currCoordinate = new CoordinateImpl(i, columIndex);
            if (selectedItems.contains(itemsInColums.get(i))) {
                Coordinate newCoordinate = new CoordinateImpl(stratIndex, columIndex);
                selectedItemsPrevAndNewCoordinates.put(currCoordinate, newCoordinate);
                stratIndex++;
            }
        }
        return selectedItemsPrevAndNewCoordinates;
    }

    public Set<String> getSelectedItems() {
        Set<String> selectedItems = new HashSet<>();
        for (CheckBox checkBox : checkBoxesItem.values()) {
            if (checkBox.isSelected()) {
                selectedItems.add(checkBox.getText());
            }
        }
        return selectedItems;
    }

    public void setColumIndex(int columIndex) {
        this.columIndex = columIndex;
    }

    public void setItemsInColums(Map<Integer, String> itemsInColums) {
        this.itemsInColums = itemsInColums;
    }


}
