package Servelts;

import Mangger.SheetManger;
import Utils.Constants;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import body.Logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.impl.RangeDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

@WebServlet(name = "RangeServelt", urlPatterns = {Constants.GET_RANGE, Constants.GET_TEMP_RANGE, Constants.GET_RANGES_NAME, Constants.ADD_NEW_RANGE, Constants.DELETE_RANGE, Constants.GET_THE_RANGE_OF_THE_RANGE, Constants.GET_COL_ITEMS})
public class RangeServelt extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.GET_RANGE -> getRange(request, response);
            case Constants.GET_RANGES_NAME -> getListOfRange(request, response);
            case Constants.ADD_NEW_RANGE -> addNewRange(request, response);
            case Constants.DELETE_RANGE -> deleteRange(request, response);
            case Constants.GET_THE_RANGE_OF_THE_RANGE -> getTheRangeOfTheRange(request, response);
            case Constants.GET_TEMP_RANGE -> getTempRange(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.GET_COL_ITEMS -> getColItems(request, response);
        }
    }

    private void getTempRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                String sheet = request.getParameter("sheetName");
                String theRange = request.getParameter("theRange");
                SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                RangeDTO range = sheetManger.getSheet(sheet).createTempRange(theRange);
                if (range != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(range);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                    response.getWriter().flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void getColItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                String sheetName = request.getHeader("sheetName");
                String range = request.getHeader("range");
                int version = Integer.parseInt(request.getHeader("version"));
                int col = Integer.parseInt(request.getHeader("col"));
                BufferedReader reader = request.getReader();
                SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                Logic logicSheet = sheetManger.getSheet(sheetName);
                Map<Integer, String> colItems;
                if (!reader.ready()) {
                   colItems = logicSheet.getColumsItem(col, range);
                }
                else {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Integer>>(){}.getType();
                    List<Integer> rowSelected = gson.fromJson(reader, listType);
                    colItems = logicSheet.getColumsItem(col, range, rowSelected);
                }
                Gson toJson = new Gson();
                String json = toJson.toJson(colItems);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
            catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            }
            }
    }

    private void getTheRangeOfTheRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                String sheetName = request.getParameter("sheetName");
                String range = request.getParameter("range");
                SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                Logic logicSheet = sheetManger.getSheet(sheetName);
                List<Integer> startEndRange = logicSheet.getTheRangeOfTheRange(range);
                Gson gson = new Gson();
                String json = gson.toJson(startEndRange);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void deleteRange(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
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
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                String sheet = request.getParameter("sheetName");
                String rangeId = request.getParameter("rangeId");
                SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                RangeDTO range = sheetManger.getSheet(sheet).getRange(rangeId);
                if (range != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(range);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                    response.getWriter().flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
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
