package Components.Commands.Graph;

import Components.Commands.CheckRange.CheckRangeController;
import Components.Commands.CommandsController;
//import expression.CellType;
//import expression.api.EffectiveValue;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.plaf.PanelUI;
import java.security.PublicKey;
import java.util.List;

public class GraphController {

    @FXML
    private VBox errorMessege;

    @FXML
    private ChoiceBox<String> graphType;

    @FXML
    private TextField xRangeText;

    @FXML
    private ChoiceBox<String> xScrollRange;

    @FXML
    private TextField xTitle;

    @FXML
    private TextField yRangeText;

    @FXML
    private ChoiceBox<String> yScrollRange;

    @FXML
    private TextField yTitle;

    private CommandsController commandsController;

    private NumberAxis xAxisLine = new NumberAxis();

    private CategoryAxis xAxisBar = new CategoryAxis();

    private NumberAxis yAxis = new NumberAxis();

    private XYChart.Series<Number, Number> seriesLine = new XYChart.Series<>();
    private XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();

    private VBox graphPopUp;

    @FXML
    void OKClicked(ActionEvent event) {
        rest();
        List<String> xaxis = getRange(xRangeText.getText());
        List<String> yaxis = getRange(yRangeText.getText());
        if(xaxis == null || yaxis == null){
            return;
        }
        errorMessege.setVisible(false);
        xAxisLine.setLabel(xTitle.getText());
        xAxisBar.setLabel(xTitle.getText());
        yAxis.setLabel(yTitle.getText());
        if(graphType.getValue() == "Line"){
            createLineGraph(xaxis, yaxis);
            focusOnDataPoint();
        }
        else{
            createBarGraph(xaxis, yaxis);
        }
        Scene scene = new Scene(graphPopUp);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void rest() {
        seriesLine.getData().clear();
        seriesBar.getData().clear();
        xAxisBar = new CategoryAxis();
        xAxisLine = new NumberAxis();
        yAxis = new NumberAxis();

    }

    private List<String> getRange(String text) {
        if(xScrollRange.getItems().contains(text)){
            return commandsController.getRange(text);
        }
        if(CheckRangeController.isRangeValid(text.toUpperCase())){
            return commandsController.getCustomRange(text.toUpperCase());
        }
        else{
            errorMessege.setVisible(true);
            return null;
        }
    }

    private boolean tryParseDouble(String value){
        try{
            Double.parseDouble(value);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private void createLineGraph(List<String> xaxis,  List<String> yaxis) {
        for(int i = 0; i < Integer.min(xaxis.size(), yaxis.size()); i++){
//            if(xaxis.get(i).getCellType() == CellType.NUMERIC && yaxis.get(i).getCellType() == CellType.NUMERIC){
//                seriesLine.getData().add(new XYChart.Data<>((Number) xaxis.get(i).getValue(), (Number) yaxis.get(i).getValue()));
//            }
            if(tryParseDouble(xaxis.get(i)) && tryParseDouble(yaxis.get(i))){
                seriesLine.getData().add(new XYChart.Data<>((Number) Double.parseDouble(xaxis.get(i)), (Number) Double.parseDouble(yaxis.get(i))));
            }
        }
        LineChart<Number, Number> lineChart = new LineChart<Number, Number>(xAxisLine, yAxis);
        lineChart.setTitle("Line Graph");
        lineChart.getData().add(seriesLine);
        graphPopUp = new VBox(lineChart);
    }

    private void focusOnDataPoint() {
        XYChart.Data<Number, Number> dataPoint = seriesLine.getData().get(0);
        double xValue = dataPoint.getXValue().doubleValue();
        double yValue = dataPoint.getYValue().doubleValue();
        // Adjust X and Y axis bounds to zoom in on the data point
        double xRange = xAxisLine.getUpperBound() - xAxisLine.getLowerBound();
        double yRange = yAxis.getUpperBound() - yAxis.getLowerBound();

        xAxisLine.setLowerBound(Math.max(xValue - xRange / 2, 0)); // Adjust bounds to include the data point
        xAxisLine.setUpperBound(xValue + xRange / 2);

        yAxis.setLowerBound(Math.max(yValue - yRange / 2, 0)); // Adjust bounds to include the data point
        yAxis.setUpperBound(yValue + yRange / 2);
    }

    private void createBarGraph(List<String> xaxis,  List<String> yaxis) {
        for(int i = 0; i < Integer.min(xaxis.size(), yaxis.size()); i++){
//            if(yaxis.get(i).getCellType() == CellType.NUMERIC){
//                seriesBar.getData().add(new XYChart.Data<>((String) xaxis.get(i).getValue().toString(), (Number) yaxis.get(i).getValue()));
//            }
            if(tryParseDouble(yaxis.get(i))){
                //seriesLine.getData().add(new XYChart.Data<>((String) xaxis.get(i), (Number) Double.parseDouble(yaxis.get(i))));
                seriesBar.getData().add(new XYChart.Data<>((String) xaxis.get(i), (Number) Double.parseDouble(yaxis.get(i))));
            }
                    }
        BarChart<String, Number> barChart = new BarChart<String, Number>(xAxisBar, yAxis);
        barChart.setTitle("Bar Graph");
        barChart.getData().add(seriesBar);
        graphPopUp = new VBox(barChart);
    }

    public void initialize(){
        xScrollRange.getItems().add("Custom");
        yScrollRange.getItems().add("Custom");
    }

    public void init() {
        setGraphType();
        addRangeToScroll();
        xRangeText.disableProperty().bind(xScrollRange.valueProperty().isEqualTo("Custom").not());
        yRangeText.disableProperty().bind(yScrollRange.valueProperty().isEqualTo("Custom").not());
        xScrollRange.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Custom")) {
                xRangeText.setText(newValue);
            }
        });
        yScrollRange.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Custom")) {
                yRangeText.setText(newValue);
            }
        });
    }

    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }

    private void setGraphType() {
        graphType.getItems().addAll("Line", "Bar");
    }

    private void addRangeToScroll(){
        List<String> ranges = commandsController.getRanges();
        xScrollRange.getItems().addAll(ranges);
        yScrollRange.getItems().addAll(ranges);
        xScrollRange.setValue("Custom");
        yScrollRange.setValue("Custom");
    }

}
