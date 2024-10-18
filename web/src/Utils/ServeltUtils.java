package Utils;

import dto.impl.PermissionType;
import Mangger.RequestPermissonManager;
import Mangger.SheetManger;
import body.Logic;
import dto.impl.PermissionRequest;
import jakarta.servlet.ServletContext;
import Mangger.UserManger;
import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.locks.Lock;

import static Utils.SessionUtils.getUserNameFromSession;

public class ServeltUtils {
    private static final Object userManagerLock = new Object();
    private static final Object sheetManagerLock = new Object();
    private static final Object permissionRequestLock = new Object();

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

    public static RequestPermissonManager getPermissionRequestManager(ServletContext servletContext){
        synchronized (permissionRequestLock){
            if(servletContext.getAttribute("permissionManager") == null){
                servletContext.setAttribute("permissionManager", new RequestPermissonManager());
            }
        }
        return (RequestPermissonManager) servletContext.getAttribute("permissionManager");
    }

    public static PermissionRequest createPermissionRequest(HttpServletRequest request){
        String usernameFromSession = getUserNameFromSession(request);
        String sheetName = request.getParameter("sheetName");
        String permission = request.getParameter("permission").toUpperCase();
        Logic logic = getSheetManger(request.getServletContext()).getSheet(sheetName);
        String owner = logic.getOwner();
        return new PermissionRequest(usernameFromSession, sheetName, PermissionType.valueOf(permission), owner);
    }


}
