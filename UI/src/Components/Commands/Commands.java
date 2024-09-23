package Components.Commands;

import Components.Commands.CheckRange.CheckRangeController;
import javafx.event.ActionEvent;

import java.io.IOException;

public interface Commands {
    boolean VClicked() throws IOException, ClassNotFoundException;
    void XClicked();
}
