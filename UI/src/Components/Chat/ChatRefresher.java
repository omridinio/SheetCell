package Components.Chat;

import dto.impl.ChatMessege;
import javafx.application.Platform;
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

public class ChatRefresher extends TimerTask {

    private int lastIndex = 0;
    private Consumer<List<ChatMessege>> updateChat;

    public ChatRefresher(Consumer<List<ChatMessege>> updateChat) {
        this.updateChat = updateChat;
    }
    @Override
    public void run() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_MESSAGES)
                .newBuilder()
                .addQueryParameter("lastIndexMessege", String.valueOf(lastIndex))
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
                    response.body().string();
                }
                else {
                    String jasonChatMesseges = response.body().string();
                    ChatMessege[] chatMesseges = Constants.GSON_INSTANCE.fromJson(jasonChatMesseges, ChatMessege[].class);
                    List<ChatMessege> chatMessegeList = List.of(chatMesseges);
                    if(chatMessegeList.size() > 0) {
                        lastIndex += chatMessegeList.size();
                        Platform.runLater(() -> {
                            updateChat.accept(chatMessegeList);
                        });
                    }
                }
            }
        });
    }
}
