package Components.Commands.Expression.TextFiled;

import Components.Commands.Expression.ExpressionController;
import Properties.CoordinateExpression;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class TextFiledController {

    @FXML
    private TextField text;

    private ExpressionController expressionController;

    private CoordinateExpression coordinateExpression;

    Text textMeasure = new Text();

    public void initialize() {
//        text.textProperty().addListener((observable, oldValue, newValue) -> {
//            textMeasure.setText(newValue);
//            double width = textMeasure.getLayoutBounds().getWidth()
//                    + textMeasure.getLayoutBounds().getWidth() / 10;
//            text.setPrefWidth(width);
//        });

        text.textProperty().addListener((observable, oldValue, newValue) -> {
            textMeasure.setText(newValue); // Update the text node with the new value
            double textWidth = textMeasure.getLayoutBounds().getWidth(); // Get the width of the new text
            text.setPrefWidth(textWidth + 10); // Set the TextField's width based on text width (+10 padding)
        });
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return text.getText();
    }

    public void setExpressionController(ExpressionController expressionController) {
        this.expressionController = expressionController;
    }

    public void setCoordinateExpression(CoordinateExpression coordinateExpression) {
        this.coordinateExpression = coordinateExpression;
    }

    public CoordinateExpression getCoordinateExpression() {
        return coordinateExpression;
    }
}
