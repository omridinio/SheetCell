package Components.Commands.Filter.FilterColums.ComboChoiceBox;

import Components.Commands.Filter.FilterColums.FilterColumsController;
import body.Coordinate;
import body.impl.CoordinateImpl;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.*;

public class ComboChoiceBoxController {

    @FXML
    private VBox item;

    @FXML
    private TextField searchBox;

    @FXML
    private CheckBox selectAll;

    private Map<String, CheckBox> checkBoxesItem = new HashMap<>();

    private int columIndex;

    private Map<Integer, String> itemsInColums = new HashMap<>();

    private FilterColumsController filterColumsController;


    public void initialize() {
        selectAll.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (CheckBox checkBox : checkBoxesItem.values()) {
                if(checkBox.isVisible())
                    checkBox.setSelected(newValue);
            }
        });

        selectAll.visibleProperty().bind(
                Bindings.createBooleanBinding(
                        () -> item.getChildren().size() > 2,
                        item.getChildren()
                )
        );

        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            // Temporary list to collect the matching checkboxes
            List<CheckBox> matchingCheckBoxes = new ArrayList<>();

            // Iterate through all checkboxes and set visibility based on search text
            for (CheckBox checkBox : checkBoxesItem.values()) {
                if (!checkBox.getText().toLowerCase().contains(newValue.toLowerCase())) {
                    checkBox.setVisible(false); // Hide checkboxes that don't match the search
                } else {
                    checkBox.setVisible(true); // Show matching checkboxes
                    matchingCheckBoxes.add(checkBox); // Collect matching checkboxes
                }
            }

            // Reorder the VBox by first removing the matching checkboxes and then re-adding them at the top
            for (CheckBox matchingCheckBox : matchingCheckBoxes) {
                item.getChildren().remove(matchingCheckBox); // Remove the checkbox from its current position
                item.getChildren().add(2, matchingCheckBox); // Add the matching checkbox to the top of the VBox
            }
        });

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
            String item = itemsInColums.get(i);
            if(item.equals("")){
                item = "Empty";
            }
            if (selectedItems.contains(item)) {
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
