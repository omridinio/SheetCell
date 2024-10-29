package Components.MangerSheet.ManngerCommands.AckOrDnyPer.OnePermission;

import Components.MangerSheet.ManngerCommands.AckOrDnyPer.AckOrDnyPerController;
import dto.impl.PermissionRequest;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;

public class OnePermissionController {

    @FXML
    private Label permission;

    @FXML
    private Label sheet;

    @FXML
    private Label userName;

    private int permissionId;

    private AckOrDnyPerController ackOrDnyPerController;

    private int index;


    @FXML
    void vClicked(ActionEvent event) {
        approvePermsiion("Approved");
    }

    @FXML
    void xClicked(ActionEvent event) {
        approvePermsiion("Denied");
    }

    private void approvePermsiion(String status) {
        String finalUrl = HttpUrl
                .parse(Constants.PERMISSON_APPROVE)
                .newBuilder()
                .addQueryParameter("status", status)
                .addQueryParameter("requestId", String.valueOf(permissionId))
                .build()
                .toString();
        Request request = new Request.Builder()
                .url(finalUrl)
                .put(RequestBody.create(null, new byte[0]))
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {

                }
                else {
                    ackOrDnyPerController.removePermission(index);
                }
                response.body().string();
            }
        });
    }


    public void createPermission(PermissionRequest permissionRequest, int index) {
        permissionId = permissionRequest.getIndex();
        permission.setText(permissionRequest.getPermission().toString());
        sheet.setText(permissionRequest.getSheetName());
        userName.setText(permissionRequest.getUsername());
        this.index = index;
    }

    public void setAckOrDnyPerController(AckOrDnyPerController ackOrDnyPerController) {
        this.ackOrDnyPerController = ackOrDnyPerController;
    }

    public void setIndex(int i) {
        index = i;
    }
}
