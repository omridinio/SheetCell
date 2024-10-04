package Utils;

import jakarta.servlet.ServletContext;
import userManger.UserManger;

import java.util.concurrent.locks.Lock;

public class ServeltUtils {
    private static final Object userManagerLock = new Object();

    public static UserManger getUserManger(ServletContext servletContext){
        synchronized (userManagerLock){
            if(servletContext.getAttribute("userManager") == null){
                servletContext.setAttribute("userManager", new UserManger());
            }
        }
        return (UserManger) servletContext.getAttribute("userManager");
    }


}
