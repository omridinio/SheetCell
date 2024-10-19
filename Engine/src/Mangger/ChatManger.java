package Mangger;

import dto.impl.ChatMessege;

import java.util.ArrayList;
import java.util.List;

public class ChatManger {
    private List<ChatMessege> chat = new ArrayList<>();
    private int numberOfMessege = 0;

    public synchronized void addMessege(ChatMessege messege){
        chat.add(messege);
        numberOfMessege++;
    }

    public List<ChatMessege> getChat(){
        return chat;
    }

    public List<ChatMessege> getChat(int lastIndexMessege){
        List<ChatMessege> res = new ArrayList<>();
        for(int i = lastIndexMessege; i < chat.size(); i++){
            res.add(chat.get(i));
        }
        return res;
    }

}
