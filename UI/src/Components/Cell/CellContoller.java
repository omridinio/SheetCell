package Components.Cell;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class CellContoller {

    @FXML
    private Button cell;


    public void setText(String text) {}

    public double getWidth() {
        return cell.getWidth();
    }

    public double getHeight() {
        return cell.getHeight();
    }
}
