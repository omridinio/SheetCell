package Servelts.LoginServelt;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import userManger.UserManger;


import java.io.IOException;


@WebServlet(name = "LoginServelt", urlPatterns = {"/login"})
public class LoginServelt extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);

        if(usernameFromSession == null) {
            String usernameFromParameter = request.getParameter("username");
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getOutputStream().print("Error: No entered username");
            } else {
                UserManger userManager = ServeltUtils.getUserManger(getServletContext());
                usernameFromParameter = usernameFromParameter.trim();
                synchronized (this) {
                    if (userManager.isUserExist(usernameFromParameter)) {
                        String errorMessage = "Username " + usernameFromSession + " already exists. Please enter a different username.";
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    } else {
                        request.getSession(true).setAttribute("username", usernameFromParameter);
                        userManager.addUser(usernameFromSession);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getOutputStream().print("User " + usernameFromSession + " added successfully");
                    }
                }
            }
        }
    }
}
