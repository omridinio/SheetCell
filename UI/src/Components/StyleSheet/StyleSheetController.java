package Components.StyleSheet;

import Components.Cell.CellContoller;
import Components.Shitcell.ShitsellController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;

import java.util.List;

public class StyleSheetController {

    @FXML
    private ComboBox<Integer> sizeFont;

    @FXML
    private ColorPicker fontColor;

    @FXML
    private ColorPicker backgroundColor;

    @FXML
    private ComboBox<String> fontsType;

    @FXML
    private AnchorPane styleSheet;





    ShitsellController sheetsetController;




    public void initialize() {
        sizeFont.getItems().addAll(List.of(8,10,12,14,16,18,20,22,24,26,28,30));
        sizeFont.setValue(12);
        fontColor.setValue(javafx.scene.paint.Color.BLACK);
        fontsType.getItems().addAll(Font.getFamilies());
        fontsType.setValue("Arial");


    }

    public void initializeStyleSheet() {
        sheetsetController.intitializeStyleSheet(this);
    }

    public int getSizeFont() {
        return sizeFont.getValue();
    }

    public String getFontColor() {
        return fontColor.getValue().toString();
    }

    public String getBackgroundColor() {
        return backgroundColor.getValue().toString();
    }

    public String getFontsType() {
        return fontsType.getValue();
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.sheetsetController = shitsellController;
    }

    @FXML
    void changeFontType(ActionEvent event) {
        sheetsetController.changeFontType(fontsType.getValue());
    }

    @FXML
    void backgroundColorChange(ActionEvent event) {
        sheetsetController.backgroundColorChange(backgroundColor.getValue());
    }

    @FXML
    void changeFontColor(ActionEvent event) {
        sheetsetController.changeFontColor(fontColor.getValue());
    }

    @FXML
    void changeSizeFont(ActionEvent event) {
        sheetsetController.changeSizeFont(sizeFont.getValue());
    }


    @FXML
    void bottomClicked(ActionEvent event) {
        sheetsetController.bootomClicked();
    }

    @FXML
    void centerClicked(ActionEvent event) {
        sheetsetController.centerClicked();
    }

    @FXML
    void leftClicked(ActionEvent event) {
        sheetsetController.leftClicked();
    }

    @FXML
    void midlleClicked(ActionEvent event) {
        sheetsetController.midlleClicked();
    }

    @FXML
    void rightClicked(ActionEvent event) {
        sheetsetController.rightClicked();
    }

    @FXML
    void topClicked(ActionEvent event) {
        sheetsetController.topClicked();
    }


    public void updateStyleSheet(CellContoller cellContoller) {
        sizeFont.setValue(cellContoller.getSizeFont());
        fontColor.setValue(javafx.scene.paint.Color.valueOf(cellContoller.getFontColor()));
        backgroundColor.setValue(javafx.scene.paint.Color.valueOf(cellContoller.getBackgroundColor()));
        if (cellContoller.getFontType() == "Arial") {
            fontsType.setValue("Arial");
        }
        else
            fontsType.setValue(cellContoller.getFontType());
    }

    public AnchorPane getStyleSheet() {
        return styleSheet;
    }






}
