package Components.Cell;

import Components.Shitcell.ShitsellController;
import body.Coordinate;
import body.Sheet;
import body.impl.CoordinateImpl;
import dto.impl.CellDTO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.*;

public class CellContoller implements Serializable {


    @FXML
    private Separator botomSperator;

    @FXML
    private Button cell;

    @FXML
    private VBox dynamicArea;

    @FXML
    private TextField jump;

    @FXML
    private Separator leftColSperate;

    @FXML
    private TextField maxValue;

    @FXML
    private TextField minValue;

    @FXML
    private Slider slider;



    private ShitsellController shitsellController;
    private CellDTO cellDTO;
    private ColumnConstraints columnConstraints;
    private RowConstraints rowConstraints;
    private boolean isTitle = false;
    private String backgroundColor = "fffefe";
    private String high = "center";
    private String width = "center";
    private int sizeFont = 12;
    private String fontColor = "000000";
    private String fontType = Font.getFamilies().get(3);

    public CellContoller() { }

    public CellContoller(CellContoller copyCell){
        this.shitsellController = copyCell.shitsellController;
        setCellDTO(copyCell.getCellDTO());
        this.fontType = copyCell.fontType;
        this.fontColor = copyCell.fontColor;
        this.sizeFont = copyCell.sizeFont;
        this.backgroundColor = copyCell.backgroundColor;
        this.high = copyCell.high;
        this.width = copyCell.width;

    }

    public CellContoller duplicate() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Components/Cell/Cell.fxml"));
        Node cell = fxmlLoader.load();
        CellContoller newController = fxmlLoader.getController();
        newController.shitsellController = this.shitsellController;
        newController.cellDTO = this.cellDTO;
        newController.columnConstraints = this.columnConstraints;
        newController.rowConstraints = this.rowConstraints;
        newController.isTitle = this.isTitle;
        newController.backgroundColor = this.backgroundColor;
        newController.high = this.high;
        newController.width = this.width;
        newController.sizeFont = this.sizeFont;
        newController.fontColor = this.fontColor;
        newController.fontType = this.fontType;
        return newController;
    }


    public void initialize() {
        updateCellDeatils();
    }

    private void initializeDynmicAnlayze() {
//        minValue.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                minValue.setText(newValue.replaceAll("[^\\d]", "")); // Remove non-digit characters
//            }
//            if(Integer.parseInt(newValue) < 0){
//                minValue.setText("0");
//            }
//            else if(Integer.parseInt(newValue) > Integer.parseInt(maxValue.getText())){
//                minValue.setText(maxValue.getText());
//            }
//            int value = Integer.parseInt(minValue.getText());
//            slider.setMin(value);
//        });

        minValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String  min = minValue.getText();
                if (!min.matches("\\d*")) {
                    minValue.setText(min.replaceAll("[^\\d]", "")); // Remove non-digit characters
                }
                if(Integer.parseInt(min) < 0){
                    minValue.setText("0");
                }
                else if(Integer.parseInt(min) > Integer.parseInt(maxValue.getText())){
                    minValue.setText(maxValue.getText());
                }
                int value = Integer.parseInt(minValue.getText());
                slider.setMin(value);
            }
        });

//        maxValue.textProperty().addListener((observable, oldValue, newValue) -> {
//            if (!newValue.matches("\\d*")) {
//                maxValue.setText(newValue.replaceAll("[^\\d]", "")); // Remove non-digit characters
//            }
//            if(Integer.parseInt(newValue) < Integer.parseInt(minValue.getText())){
//                maxValue.setText(minValue.getText());
//            }
//            int value = Integer.parseInt(maxValue.getText());
//            slider.setMax(value);
//        });

        maxValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String  max = maxValue.getText();
                if (!max.matches("\\d*")) {
                    maxValue.setText(max.replaceAll("[^\\d]", "")); // Remove non-digit characters
                }
                if(Integer.parseInt(max) < Integer.parseInt(minValue.getText())){
                    maxValue.setText(minValue.getText());
                }
                int value = Integer.parseInt(maxValue.getText());
                slider.setMax(value);
            }
        });

        jump.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                jump.setText(newValue.replaceAll("[^\\d]", "")); // Remove non-digit characters
            }
            int value = Integer.parseInt(jump.getText());
            slider.setBlockIncrement(value);
            slider.setMajorTickUnit(value);
            slider.setMinorTickCount(0);
            slider.setSnapToTicks(true); 
        });
    }


    @FXML
    void clicked(ActionEvent event) {
        shitsellController.getSheetArea().requestFocus();
        if(isTitle){
            shitsellController.titleClicked(this);
        }
        else {
            shitsellController.cellClicked(cellDTO, cell);
        }
    }

    public CellContoller copyCell() throws IOException, ClassNotFoundException {
        // Step 1: Serialize the object to a byte array
        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
        outStream.writeObject(this);
        outStream.flush();

        // Step 2: Deserialize the byte array into a new object
        ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
        ObjectInputStream inStream = new ObjectInputStream(byteInStream);

        CellContoller newContoller = (CellContoller) inStream.readObject();
        return  newContoller;
    }

    public void setFontType(String fontType) {
        this.fontType = fontType;
        cell.setStyle("-fx-font-family: " + fontType + ";");
        updateCellDeatils();
    }

    public void setFontColor(String fontColor) {
        this.fontColor = fontColor;
        cell.setStyle("-fx-text-fill: #" + fontColor + ";");
        updateCellDeatils();
    }

    public void setSizeFont(int sizeFont) {
        this.sizeFont = sizeFont;
        cell.setStyle("-fx-font-size: " + sizeFont + "px;");
        updateCellDeatils();
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        updateCellDeatils();
    }

    public void updateCellDeatils() {
        cell.setFont(Font.font(fontType, sizeFont));
        cell.setStyle("-fx-background-color: #" + backgroundColor + ";"
                + "-fx-font-size: " + sizeFont + "px;"
                + "-fx-text-fill: #" + fontColor + ";"
                + "-fx-font-family: " + fontType + ";");
    }

    public void updateDeatils(String color){
        cell.setStyle("-fx-background-color: " + color + ";"
                + "-fx-font-size: " + sizeFont + "px;"
                + "-fx-text-fill: #" + fontColor + ";"
                + "-fx-font-family: " + fontType + ";");
    }

    public void setDependOnThem(){
        String gradientColor = "linear-gradient(rgba(135, 206, 250, 0.4), rgba(135, 206, 250, 0.3), #" + backgroundColor + ")";;
        updateDeatils(gradientColor);
    }

    public void setDependOnHim(){
        String gradientColor = "linear-gradient(rgba(0, 255, 0, 0.4), rgba(0, 255, 0, 0.3), #" + backgroundColor + ")";
        updateDeatils(gradientColor);
    }

    public void setSelect(){
        String gradientColor = "linear-gradient(rgba(255, 228, 225, 0.86), rgba(255, 228, 225, 0.6), #" + backgroundColor + ")";
        updateDeatils(gradientColor);
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void calculatecoord(){
        if (high == "center" && width == "center"){
            cell.setAlignment(Pos.CENTER);
        }
        else if(high == "top" && width == "left") {
            cell.setAlignment(Pos.TOP_LEFT);
        }
        else if(high == "top" && width == "center") {
            cell.setAlignment(Pos.TOP_CENTER);
        }
        else if(high == "top" && width == "right") {
            cell.setAlignment(Pos.TOP_RIGHT);
        }
        else if(high == "center" && width == "left") {
            cell.setAlignment(Pos.CENTER_LEFT);
        }
        else if(high == "center" && width == "right") {
            cell.setAlignment(Pos.CENTER_RIGHT);
        }
        else if(high == "bottom" && width == "left") {
            cell.setAlignment(Pos.BOTTOM_LEFT);
        }
        else if(high == "bottom" && width == "center") {
            cell.setAlignment(Pos.BOTTOM_CENTER);
        }
        else if(high == "bottom" && width == "right") {
            cell.setAlignment(Pos.BOTTOM_RIGHT);
        }
    }

    public void setShitsellController(ShitsellController shitsellController){
        this.shitsellController = shitsellController;
    }

    public void setCellDTO(CellDTO cellDTO){
        this.cellDTO = cellDTO;
        setText(cellDTO.getOriginalEffectiveValue().toString());
    }

    public Button getCell() {
        return cell;
    }

    public String getText() {
        return cell.getText();
    }

    public void setIsTitle(boolean isTitle) {
        this.isTitle = isTitle;
    }

    public void setText(String text) {
        cell.setText(text);
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public int getSizeFont() {
        return sizeFont;
    }

    public String getFontColor() {
        return fontColor;
    }

    public String getFontType() {
        return fontType;
    }

    public void setColumnConstraints(ColumnConstraints columnConstraints) {
        this.columnConstraints = columnConstraints;
    }

    public void setRowConstraints(RowConstraints rowConstraints) {
        this.rowConstraints = rowConstraints;
    }

    @FXML
    void onLeftColumnDragged(MouseEvent event) {
        double initialX = (double) leftColSperate.getUserData();
        double deltaX = event.getSceneX() - initialX;
        double newWidth = columnConstraints.getPrefWidth() + deltaX;
        newWidth = Math.max(newWidth, 10);
        newWidth = Math.min(newWidth, 300);
        columnConstraints.setPrefWidth(newWidth);
        leftColSperate.setUserData(event.getSceneX());
    }

    @FXML
    void onLeftMousePressed(MouseEvent event) {
        leftColSperate.setUserData(event.getSceneX());
    }

    public void ableColSpearte() {
        leftColSperate.setDisable(false);
        leftColSperate.setVisible(true);
    }

    public void ableRowSpearte() {
        botomSperator.setDisable(false);
        botomSperator.setVisible(true);
    }

    @FXML
    void onBotomDrag(MouseEvent event) {
        double initialY = (double) botomSperator.getUserData();
        double deltaY = event.getSceneY() - initialY;
        double newHight = rowConstraints.getPrefHeight() + deltaY;
        newHight = Math.max(newHight, 10);
        newHight = Math.min(newHight, 300);
        rowConstraints.setPrefHeight(newHight);
        botomSperator.setUserData(event.getSceneY());
    }

    @FXML
    void onBotomPresed(MouseEvent event) {
        botomSperator.setUserData(event.getSceneY());
    }


    public CellDTO getCellDTO() {
        return cellDTO;
    }

    public String getHigh() {
        return high;
    }

    public String getWidth() {
        return width;
    }

    public void copyCell(CellContoller cellcopy) {
        setCellDTO(cellcopy.getCellDTO());
        this.fontType = cellcopy.fontType;
        this.fontColor = cellcopy.fontColor;
        this.sizeFont = cellcopy.sizeFont;
        this.backgroundColor = cellcopy.backgroundColor;
        this.high = cellcopy.high;
        this.width = cellcopy.width;

    }

    public void restCell() {
        cell.setText("");
        fontType = "Ariel";
        fontColor = "000000";
        sizeFont = 12;
        backgroundColor = "fffefe";
        high = "center";
        width = "center";
        updateCellDeatils();
        calculatecoord();
    }

    public void restCellArtitube(){
        fontType = "Ariel";
        fontColor = "000000";
        sizeFont = 12;
        backgroundColor = "fffefe";
        high = "center";
        width = "center";
        updateCellDeatils();
        calculatecoord();
    }


    private boolean isNaturalNumber(String str) {
       return cellDTO.getOriginalEffectiveValue() instanceof Number;
    }

    public void turnOnDynmicAnlayze() {
        dynamicArea.setVisible(true);
        dynamicArea.setDisable(false);
        int value = Integer.parseInt(cell.getText());
        minValue.setText((value - 50) + "");
        maxValue.setText((value + 50) + "");
        jump.setText("5");
        slider.setValue(value);
        slider.setMin(value - 50);
        slider.setMax(value + 50);
        slider.setBlockIncrement(5);
        slider.setMajorTickUnit(5);
        slider.setMinorTickCount(0);
        slider.setSnapToTicks(true);
        initializeDynmicAnlayze();
        high = "top";
        calculatecoord();
        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double currentValue = newValue.doubleValue();
            double move = Double.parseDouble(jump.getText());
            double roundedValue = Math.round(currentValue / move) * move;
            slider.setValue(roundedValue);
            shitsellController.updateCellDynmicAnlyaze(slider.getValue(), cellDTO.getId());
        });
    }
}
