package userManger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UserManger {
    private Set<String> users = new HashSet<>();

    public synchronized void addUser(String username){
        users.add(username);
    }

    public synchronized void removeUser(String username){
        users.remove(username);
    }

    public synchronized boolean isUserExist(String username){
        return users.contains(username);
    }
}
