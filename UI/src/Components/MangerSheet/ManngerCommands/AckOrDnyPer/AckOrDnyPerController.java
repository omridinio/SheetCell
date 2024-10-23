package Components.MangerSheet.ManngerCommands.AckOrDnyPer;

import Components.MangerSheet.ManngerCommands.AckOrDnyPer.OnePermission.OnePermissionController;
import dto.impl.PermissionRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AckOrDnyPerController {

    @FXML
    private FlowPane permissionArea;


    private Map<Integer, Parent> permissions = new HashMap<>();



    public void insertPermissions(List<PermissionRequest> permissions) throws IOException {
        for (PermissionRequest permission : permissions) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Components/MangerSheet/ManngerCommands/AckOrDnyPer/OnePermission/onePermission.fxml"));
            Parent newWindowRoot = loader.load();
            OnePermissionController onePermissionController = loader.getController();
            onePermissionController.setAckOrDnyPerController(this);
            onePermissionController.createPermission(permission, permission.getIndex());
            Platform.runLater(() -> permissionArea.getChildren().add(newWindowRoot));
            this.permissions.put(permission.getIndex(), newWindowRoot);
        }
    }

    public void removePermission(int index) {
        Platform.runLater(() -> {
            permissionArea.getChildren().remove(permissions.get(index));
            permissions.remove(index);
        });
    }
}
