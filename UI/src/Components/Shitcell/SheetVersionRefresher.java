package Components.Shitcell;


import Components.Error.ErrorController;
import javafx.beans.property.BooleanProperty;
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

public class SheetVersionRefresher extends TimerTask {
    private int version;
    private Consumer<Integer> updateVersion;
    private String sheetName;

    public SheetVersionRefresher(int version, Consumer<Integer> updateVersion, String sheetName) {
        this.version = version;
        this.updateVersion = updateVersion;
        this.sheetName = sheetName;
    }

    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.REFRESH_SHEET_VERSIONS)
                .newBuilder()
                .addQueryParameter("sheetName", sheetName)
                .build()
                .toString();
        HttpClientUtil.runAsync(finalUrl, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code() != 200) {
                    ErrorController.showError(response.body().string());
                }
                else {
                    int newVersion = Integer.parseInt(response.body().string());
                    if (newVersion > version) {
                        updateVersion.accept(newVersion);
                        version = newVersion;
                    }
                }
            }
        });
    }
}
