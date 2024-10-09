package Servelts;

import Mangger.SheetManger;
import Utils.Constants;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import body.Logic;
import com.google.gson.Gson;
import dto.impl.RangeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "RangeServelt", urlPatterns = {Constants.GET_RANGE, Constants.GET_RANGES_NAME, Constants.ADD_NEW_RANGE, Constants.DELETE_RANGE})
public class RangeServelt extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.GET_RANGE -> getRange(request, response);
            case Constants.GET_RANGES_NAME -> getListOfRange(request, response);
            case Constants.ADD_NEW_RANGE -> addNewRange(request, response);
            case Constants.DELETE_RANGE -> deleteRange(request, response);

        }
    }

    private void deleteRange(HttpServletRequest request, HttpServletResponse response) {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                String sheetName = request.getParameter("sheetName");
                String rangeId = request.getParameter("rangeId");
                int currVersion = Integer.parseInt(request.getParameter("version"));
                synchronized (this) {
                    SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                    Logic logicSheet = sheetManger.getSheet(sheetName);
                    if(logicSheet.getVersion() > currVersion){
                        response.getOutputStream().print("Error: A more recent version of the sheet is available");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else{
                        logicSheet.removeRange(rangeId);
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void addNewRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                String sheetName = request.getParameter("sheetName");
                String rangeId = request.getParameter("rangeId");
                String theRange = request.getParameter("theRange");
                int currVersion = Integer.parseInt(request.getParameter("version"));
                synchronized (this) {
                    SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                    Logic logicSheet = sheetManger.getSheet(sheetName);
                    if(logicSheet.getVersion() > currVersion){
                        response.getOutputStream().print("Error: A more recent version of the sheet is available");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                    else{
                        logicSheet.createNewRange(rangeId, theRange);
                        RangeDTO range = logicSheet.getRange(rangeId);
                        Gson gson = new Gson();
                        String json = gson.toJson(range);
                        response.setContentType("application/json");
                        response.getWriter().write(json);
                        response.getWriter().flush();
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }


        }
    }

    private void getRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheet = request.getParameter("sheetName");
            String rangeId = request.getParameter("rangeId");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            RangeDTO range = sheetManger.getSheet(sheet).getRange(rangeId);
            if(range != null){
                Gson gson = new Gson();
                String json = gson.toJson(range);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void getListOfRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        String sheet = request.getParameter("sheetName");
        SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
        List<String> ranges = sheetManger.getSheet(sheet).getRangesName();
        Gson gson = new Gson();
        String json = gson.toJson(ranges);
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.getWriter().flush();
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
