package Components.MangerSheet.PermissionsTable;

import dto.impl.PermissionRequest;
import dto.impl.SheetBasicData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class PermissionRefresh extends TimerTask {
    private Consumer<List<PermissionRequest>> updatePermission;
    private BooleanProperty shouldUpdate = new SimpleBooleanProperty(false);
    private StringProperty sheetName =  new SimpleStringProperty();

    public PermissionRefresh(Consumer<List<PermissionRequest>> updatePermission, BooleanProperty shouldUpdate, StringProperty sheetName) {
        this.updatePermission = updatePermission;
        this.shouldUpdate.bind(shouldUpdate);
        this.sheetName.bind(sheetName);
    }

    @Override
    public void run() {
        if (shouldUpdate.get()) {
            String finalUrl = HttpUrl
                    .parse(Constants.PERMISSON_REFRESH)
                    .newBuilder()
                    .addQueryParameter("sheetName", sheetName.get())
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if(response.code() == 200) {
                        String jsonArrayOfSheetNames = response.body().string();
                        PermissionRequest[] permissionRequest = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, PermissionRequest[].class);
                        List<PermissionRequest> permissionRequestList = List.of(permissionRequest);
                        updatePermission.accept(permissionRequestList);
                    }
                }
            });
        }
    }
}
