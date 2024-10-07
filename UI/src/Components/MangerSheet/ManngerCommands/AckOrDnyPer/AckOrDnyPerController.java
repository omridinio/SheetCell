package Components.MangerSheet.ManngerCommands.AckOrDnyPer;

import Components.Commands.SetCommand.SetCommandController;
import Components.MangerSheet.ManngerCommands.AckOrDnyPer.OnePermission.OnePermissionController;
import dto.impl.PermissionRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;


import java.io.IOException;
import java.util.List;

public class AckOrDnyPerController {

    @FXML
    private FlowPane permissionArea;

    int count = 0;



    public void insertPermissions(List<PermissionRequest> permissions) throws IOException {
        for (PermissionRequest permission : permissions) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/ManngerCommands/AckOrDnyPer/OnePermission/onePermission.fxml"));
            Parent newWindowRoot = loader.load();
            OnePermissionController onePermissionController = loader.getController();
            onePermissionController.setAckOrDnyPerController(this);
            onePermissionController.createPermission(permission, count++);
            Platform.runLater(() -> permissionArea.getChildren().add(newWindowRoot));
        }
    }

    public void removePermission(int index) {
        Platform.runLater(() -> permissionArea.getChildren().remove(index));
    }
}
