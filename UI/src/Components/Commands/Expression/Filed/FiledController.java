package Components.Commands.Expression.Filed;

import Components.Cell.CellContoller;
import Components.Commands.Expression.ExpressionController;
import Properties.ExpressionUi;
import Properties.ExpressionUiImpl;
import Properties.Operator;
import Properties.TargetExpression;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FiledController {

    @FXML
    private HBox ex1;

    @FXML
    private HBox ex2;

    @FXML
    private HBox ex3;

    @FXML
    private ChoiceBox<Operator> function1;

    @FXML
    private ChoiceBox<Operator> function2;

    @FXML
    private ChoiceBox<Operator> function3;

    @FXML
    private TitledPane title1;

    @FXML
    private TitledPane title2;

    @FXML
    private TitledPane title3;

    private List<TextField> textFields = new ArrayList<>(Collections.nCopies(3, null));

    private ExpressionUi expressionUi;

    private String id;

    private List<FiledController> filedControllers =new ArrayList<>(Collections.nCopies(3, null));

    ExpressionController expressionController;

    private String expressionNumber;


    public void initialize() {
        function1.getItems().addAll(Operator.values());
        function2.getItems().addAll(Operator.values());
        function3.getItems().addAll(Operator.values());
        function1.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                chooseOperator(function1, ex1, 0);
            } catch (IOException e) {

            }
        });
        function2.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                chooseOperator(function2, ex2, 1);
            } catch (IOException e) {

            }
        });
        function3.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                chooseOperator(function3, ex3, 2);
            } catch (IOException e) {

            }
        });

    }

    public void setExpressionController(ExpressionController expressionController){
        this.expressionController = expressionController;
    }

    public void createNewExpression(Operator operator) {
        expressionUi = new ExpressionUiImpl(operator);
    }

    public void OneExpression(String expressionNumber) {
        String title1Str = expressionNumber + ".1";
        title1.setVisible(true);
        title1.setText(title1Str);
    }

    public void TwoExpression(String expressionNumber) {
        String title1Str = expressionNumber + ".1";
        String title2Str = expressionNumber + ".2";
        title1.setVisible(true);
        title2.setVisible(true);
        title1.setText(title1Str);
        title2.setText(title2Str);
    }

    public void ThreeExpression(String expressionNumber) {
        String title1Str = expressionNumber + ".1";
        String title2Str = expressionNumber + ".2";
        String title3Str = expressionNumber + ".3";
        title1.setVisible(true);
        title2.setVisible(true);
        title3.setVisible(true);
        title1.setText(title1Str);
        title2.setText(title2Str);
        title3.setText(title3Str);
    }

    public ChoiceBox<Operator> getFunction1() {
        return function1;
    }

    public ChoiceBox<Operator> getFunction2() {
        return function2;
    }

    public ChoiceBox<Operator> getFunction3() {
        return function3;
    }

    private String getNumberExpression(int index){
        switch(index){
            case 0:
                return title1.getText();
            case 1:
                return title2.getText();
            case 2:
                return title3.getText();
        }
        return "";
    }

    private void chooseOperator(ChoiceBox<Operator> function, HBox ex, int index) throws IOException {
        if(function.getValue() == Operator.CUSTOM){
            TextField custom = new TextField();
            if(ex.getChildren().size() > 1) {
                ex.getChildren().remove(1);
            }
            ex.getChildren().addLast(custom);
            textFields.set(index, custom);
            expressionUi.setExpression(index, new TargetExpression(""));
            custom.textProperty().addListener((observable, oldValue, newValue) -> {
                expressionUi.setExpression(index, new TargetExpression(newValue));
            });
        }
        else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Expression/Filed/filed.fxml"));
            Node express = loader.load();
            FiledController filedController = loader.getController();
            filedControllers.set(index, filedController);
            switch (Operator.valueOf(function.getValue().toString())){
                case TIMES:
                case DIVIDE:
                case MOD:
                case POW:
                case CONCAT:
                case EQUAL:
                case BIGGER:
                case LESS:
                case OR:
                case AND:
                case PERCENT:
                case PLUS:
                case MINUS:
                    filedController.TwoExpression(getNumberExpression(index));
                    break;
                case SUB:
                case IF:
                    filedController.ThreeExpression(getNumberExpression(index));
                    break;
                default:
                    filedController.OneExpression(getNumberExpression(index));
            }
            filedController.createNewExpression(function.getValue());
            expressionUi.setExpression(index, filedController.getExpressionUi());
            if(ex.getChildren().size() > 1)
                ex.getChildren().remove(1);
            ex.getChildren().add(express);
        }
    }

    public ExpressionUi getExpressionUi() {
        return expressionUi;
    }


}
