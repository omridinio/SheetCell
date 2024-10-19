package Servelts;

import Mangger.ChatManger;
import Utils.Constants;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import com.google.gson.Gson;
import dto.impl.ChatMessege;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ChatServelt", urlPatterns = {Constants.SEND_MESSAGE, Constants.GET_MESSAGES})
public class ChatServelt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.SEND_MESSAGE:
                sendMessage(request, response);
                break;
        }
    }

    private void sendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            ChatManger chatManger = ServeltUtils.getChatManger(getServletContext());
            Gson fromJson = new Gson();
            ChatMessege chatMessege = fromJson.fromJson(request.getReader(), ChatMessege.class);
            chatManger.addMessege(chatMessege);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.GET_MESSAGES:
                getMessages(request, response);
                break;
        }
    }

    private void getMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            ChatManger chatManger = ServeltUtils.getChatManger(getServletContext());
            int lastIndexMessege = Integer.parseInt(request.getParameter("lastIndexMessege"));
            List<ChatMessege> chatMesseges = chatManger.getChat(lastIndexMessege);
            Gson gson = new Gson();
            String json = gson.toJson(chatMesseges);
            response.setContentType("application/json");
            response.getWriter().write(json);
            response.getWriter().flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }


}
