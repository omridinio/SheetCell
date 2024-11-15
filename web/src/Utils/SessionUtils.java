package Utils;

import body.Sheet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {


    public static String getUserNameFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("username") : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static Sheet getDynmicSheet(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("dynamicSheet") : null;
        return sessionAttribute != null ? (Sheet) sessionAttribute : null;
    }

    public static String getSheetNameFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("sheetName") : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static int getCurrVersionFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("version") : null;
        return sessionAttribute != null ? (int) sessionAttribute : -1;
    }



}
