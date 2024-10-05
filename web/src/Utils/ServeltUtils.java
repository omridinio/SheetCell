package Utils;

import Mangger.SheetManger;
import jakarta.servlet.ServletContext;
import Mangger.UserManger;

public class ServeltUtils {
    private static final Object userManagerLock = new Object();
    private static final Object sheetManagerLock = new Object();

    public static UserManger getUserManger(ServletContext servletContext){
        synchronized (userManagerLock){
            if(servletContext.getAttribute("userManager") == null){
                servletContext.setAttribute("userManager", new UserManger());
            }
        }
        return (UserManger) servletContext.getAttribute("userManager");
    }

    public static SheetManger getSheetManger(ServletContext servletContext){
        synchronized (sheetManagerLock){
            if(servletContext.getAttribute("sheetManager") == null){
                servletContext.setAttribute("sheetManager", new SheetManger());
            }
        }
        return (SheetManger) servletContext.getAttribute("sheetManager");
    }

}
