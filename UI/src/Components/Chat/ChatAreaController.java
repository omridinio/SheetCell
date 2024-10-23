package Components.Chat;

import Components.Error.ErrorController;
import Components.MangerSheet.ManggerSheetController;
import com.google.gson.Gson;
import dto.impl.ChatMessege;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import utils.Constants;
import utils.HttpClientUtil;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatAreaController {

    @FXML
    private TextArea chatArea;

    @FXML
    private Button send;

    @FXML
    private TextArea text;

    private ManggerSheetController manggerSheetController;

    private TimerTask chatAreaRefresher;

    private Timer timer;

    @FXML
    void sendClicked(ActionEvent event) {
        String userName = manggerSheetController.getUserName().getValue();
        String message = text.getText();
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = currentTime.format(formatter);
        ChatMessege chatMessege = new ChatMessege(userName, message, time);
        sendMessege(chatMessege);
    }

    public void initialize() {
        chatArea.setEditable(false);
        send.disableProperty().bind(text.textProperty().isEmpty());
    }

    public void init() {
        stratSheetRefresher();
    }

    private void sendMessege(ChatMessege chatMessege) {
        Gson gson = new Gson();
        String json = gson.toJson(chatMessege);
        RequestBody body = RequestBody.create(
                json,
                MediaType.parse("application/json")
        );
        Request request = new Request.Builder()
                .url(Constants.SEND_MESSAGE)
                .post(body)
                .build();
        HttpClientUtil.runAsync(request, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    text.clear();
                    response.body().close();
                } else {
                    ErrorController.showError(response.body().string());
                }
            }
        });
    }

    private void updateChatArea(List<ChatMessege> chatMesseges) {
        for(ChatMessege chatMessege : chatMesseges) {
            chatArea.appendText(chatMessege.toString() + "\n");
        }
    }

    public void setManggerSheetController(ManggerSheetController manggerSheetController) {
        this.manggerSheetController = manggerSheetController;
    }

    private void stratSheetRefresher(){
        chatAreaRefresher = new ChatRefresher(this::updateChatArea);
        timer = new Timer();
        timer.schedule(chatAreaRefresher, 100, 500);
    }

    public void close() {
        if(chatAreaRefresher != null && timer != null) {
            chatAreaRefresher.cancel();
            timer.cancel();
        }
    }
}
