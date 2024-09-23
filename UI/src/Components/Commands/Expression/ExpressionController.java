package Components.Commands.Expression;

import Components.Commands.CommandsController;
import Components.Commands.Expression.Filed.FiledController;
import Properties.ExpressionUi;
import Properties.ExpressionUiImpl;
import Properties.Operator;
import Properties.TargetExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpressionController {

    @FXML
    private TextField actionLine;

    @FXML
    private HBox ex;

    @FXML
    private ChoiceBox<Operator> function;

    @FXML
    private Button ok;

    @FXML
    private Label predict;

    @FXML
    private ScrollPane scrollPane;


    private BooleanProperty error = new SimpleBooleanProperty(true);

    private CommandsController commandsController;

    private FiledController filedController;

    private ExpressionUi expressionUi;

    private String expressionNumber ="1";

    public void initialize() throws IOException {
        function.getItems().addAll(Operator.values());
        function.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(function.getValue() == Operator.CUSTOM){
                TextField custom = new TextField();
                if(ex.getChildren().size() > 1)
                    ex.getChildren().remove(1);
                ex.getChildren().add(custom);
                expressionUi = new TargetExpression(custom.getText());
                actionLine.textProperty().unbind();
                actionLine.textProperty().bind(expressionUi.getExpression());
                custom.textProperty().addListener((observable1, oldValue1, newValue1) -> {
                    expressionUi.setExpression(0,new TargetExpression(newValue1));
                    actionLine.textProperty().unbind();
                    actionLine.textProperty().bind(expressionUi.getExpression());
                });
            }
            else{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/Commands/Expression/Filed/filed.fxml"));
                Node cell = null;
                try {
                    cell = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                filedController = loader.getController();
                filedController = filedController;
                filedController.createNewExpression(function.getValue());
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
                        filedController.TwoExpression(expressionNumber);
                        break;
                    case SUB:
                    case IF:
                        filedController.ThreeExpression(expressionNumber);
                        break;
                    default:
                        filedController.OneExpression(expressionNumber);

                }
                //expressionUi = new ExpressionUiImpl(filedController.getExpression(), function.getValue());
                if(ex.getChildren().size() > 1)
                    ex.getChildren().remove(1);
                ex.getChildren().add(cell);
                expressionUi = filedController.getExpressionUi();
                actionLine.textProperty().unbind();
                actionLine.textProperty().bind(expressionUi.getExpression());
            }
        });

        actionLine.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isEmpty())
                predict.setText("");
            else {
                try {
                    predict.setText(commandsController.predictCalculate(newValue));
                    error.setValue(false);
                } catch (Exception e) {
                    error.setValue(true);
                    predict.setText(e.getMessage());
                }
            }
        });
        ok.disableProperty().bind(error);
    }

    @FXML
    void okClicked() throws IOException {
        commandsController.addExpression(actionLine.getText());
        Stage stage = (Stage) ok.getScene().getWindow();
        stage.close();

    }

    public void setCommandsController(CommandsController commandsController) {
        this.commandsController = commandsController;
    }
}
