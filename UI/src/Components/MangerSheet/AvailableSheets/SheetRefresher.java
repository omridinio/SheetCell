package Components.MangerSheet.AvailableSheets;

import Components.Error.ErrorController;
import dto.impl.SheetBasicData;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import java.util.logging.ErrorManager;

public class SheetRefresher extends TimerTask {
    private Consumer<List<SheetBasicData>> updateSheetList;
    BooleanProperty shouldUpdate = new SimpleBooleanProperty(false);
    private int index = 0;

    public SheetRefresher(Consumer<List<SheetBasicData>> updateSheetList, BooleanProperty shouldUpdate) {
        this.updateSheetList = updateSheetList;
        this.shouldUpdate.bind(shouldUpdate);
    }

    @Override
    public void run() {
        if (shouldUpdate.get()) {
            String finalUrl = HttpUrl
                    .parse(Constants.REFRESH_SHEET)
                    .newBuilder()
                    .addQueryParameter("index", index + "")
                    .build()
                    .toString();
            HttpClientUtil.runAsync(finalUrl, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    if (response.code() != 200) {
                    }
                    else {
                        String jsonArrayOfSheetNames = response.body().string();
                        SheetBasicData[] sheetBasicData = Constants.GSON_INSTANCE.fromJson(jsonArrayOfSheetNames, SheetBasicData[].class);
                        List<SheetBasicData> sheetBasicDataList = List.of(sheetBasicData);
                        //index += sheetBasicDataList.size();
                        updateSheetList.accept(sheetBasicDataList);
                    }
                }
            });
        }
    }
}
