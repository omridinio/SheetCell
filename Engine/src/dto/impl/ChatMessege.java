package dto.impl;


public class ChatMessege {
    String username;
    String message;
    String time;

    public ChatMessege(String username, String message, String time) {
        this.username = username;
        this.message = message;
        this.time = time;
    }

    @Override
    public String toString() {
        return time + " " + username + ": " + message;
    }

}
