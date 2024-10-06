package Components.MangerSheet.PermissionsTable;

import dto.impl.SheetBasicData;
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
    private Consumer<List<SheetBasicData>> updatePermission;

    public PermissionRefresh(Consumer<List<SheetBasicData>> updatePermission) {
        this.updatePermission = updatePermission;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.PERMISSON_REFRESH)
                .newBuilder()
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String jsonArrayOfSheetNames = response.body().string();
                SheetBasicData[] sheetBasicData = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, SheetBasicData[].class);
                List<SheetBasicData> sheetBasicDataList = List.of(sheetBasicData);
            }
        });
    }
}
