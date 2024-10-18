package Servelts;

import Mangger.CoordinateAdapter;
import Mangger.SheetManger;
import Utils.Constants;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import body.Logic;
import body.Sheet;
import body.impl.Coordinate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;


@WebServlet(name = "sheetServelt", urlPatterns = {Constants.VIEW_SHEET, Constants.GET_UPDATE_SHEET_VERSION, Constants.REFRESH_SHEET_VERSIONS, Constants.UPDATE_CELL, Constants.SORT, Constants.FILTER, Constants.DYNMIC_ANLYZE, Constants.DELETE_DYNAMIC_SHEET, Constants.SHEET_BY_VERSION, Constants.PREDICT_CALCULATE})
public class SheetServelt extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (request.getServletPath()) {
            case Constants.VIEW_SHEET:
                viewSheet(request, response);
                break;
            case Constants.UPDATE_CELL:
                updateCell(request, response);
                break;
            case Constants.FILTER:
                filter(request, response);
                break;
            case Constants.DYNMIC_ANLYZE:
                dynamicAnalyze(request, response);
                break;
            case Constants.DELETE_DYNAMIC_SHEET:
                deleteDynamicSheet(request, response);
                break;
            case Constants.SHEET_BY_VERSION:
                sheetByVersion(request, response);
                break;
            case Constants.PREDICT_CALCULATE:
                predictCalculate(request, response);
                break;
            case Constants.REFRESH_SHEET_VERSIONS:
                refreshSheetVersions(request, response);
                break;
            case Constants.GET_UPDATE_SHEET_VERSION:
                getUpdateSheetVersion(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        switch (request.getServletPath()) {
            case Constants.SORT:
                sort(request, response);
                break;
        }
    }

    private void getUpdateSheetVersion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                Logic sheet = sheetManger.getSheet(sheetName);
                SheetDTO sheetDTO = sheet.getSheet();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                String json = gson.toJson(sheetDTO);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void refreshSheetVersions(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                Logic sheet = sheetManger.getSheet(sheetName);
                response.getOutputStream().print(sheet.getVersion());
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void predictCalculate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                String cellId = request.getParameter("cellId");
                String expression = request.getParameter("expression");
                Logic sheet = sheetManger.getSheet(sheetName);
                String predictCalculate = sheet.predictCalculate(expression, cellId);
                response.getOutputStream().print(predictCalculate);
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void sheetByVersion(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                int version = Integer.parseInt(request.getParameter("version"));
                Logic sheet = sheetManger.getSheet(sheetName);
                SheetDTO sheetByVersion = sheet.getSheetbyVersion(version);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                String json = gson.toJson(sheetByVersion);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void deleteDynamicSheet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                Sheet dynamicSheet = SessionUtils.getDynmicSheet(request);
                if(dynamicSheet != null){
                    request.getSession(true).removeAttribute("dynamicSheet");
                }
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
    }

    private void dynamicAnalyze(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            try {
                Sheet dynamicSheet = SessionUtils.getDynmicSheet(request);
                if(dynamicSheet == null){
                    String sheetName = request.getParameter("sheetName");
                    int version = Integer.parseInt(request.getParameter("version"));
                    Logic currSheet = ServeltUtils.getSheetManger(getServletContext()).getSheet(sheetName);
                    dynamicSheet = currSheet.copySheetByVersion(version);
                    request.getSession(true).setAttribute("dynamicSheet", dynamicSheet);
                }
                String cellId = request.getParameter("cellId");
                String value = request.getParameter("value");
                dynamicSheet.dynmicAnlayzeUpdate(cellId, value);
                SheetDTO newSheet = new ImplSheetDTO(dynamicSheet);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                String json = gson.toJson(newSheet);
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

    private void filter(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                String range = request.getParameter("range");
                int version = Integer.parseInt(request.getParameter("version"));
                Logic sheet = sheetManger.getSheet(sheetName);
                List<Coordinate> coordinateInRange = sheet.getCoordinateInRange(version, range);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                String json = gson.toJson(coordinateInRange);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);

            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void sort(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            String sheetName = request.getHeader("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                Logic sheet = sheetManger.getSheet(sheetName);
                String theRange = request.getHeader("range");
                String version = request.getHeader("version");
                int versionNum = Integer.parseInt(version);
                BufferedReader reader = request.getReader();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Integer>>(){}.getType();
                List<Integer> dominantCol = gson.fromJson(reader, listType);
                Map<Coordinate, CellDTO> sortedSheet = sheet.getSortRange(versionNum, theRange, dominantCol);
                Gson resGson = new GsonBuilder()
                        .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                        .create();
                String json = resGson.toJson(sortedSheet);
                response.setContentType("application/json");
                response.getWriter().write(json);
                response.getWriter().flush();
                response.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }

    private void updateCell(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else{
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock writeLock = sheetManger.getWriteLock(sheetName);
            try {
                writeLock.lock();
                Logic sheet = sheetManger.getSheet(sheetName);
                String cellId = request.getParameter("cellId");
                String newValue = request.getParameter("newValue");
                int currVersion = Integer.parseInt(request.getParameter("version"));
                int originalVersion = sheet.getVersion();
                if (originalVersion > currVersion) {
                    response.getOutputStream().print("Error: A more recent version of the sheet is available");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    sheet.updateCell(cellId, newValue, usernameFromSession);
                    SheetDTO newSheet = sheet.getSheet();
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                            .create();
                    String json = gson.toJson(newSheet);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                    response.getWriter().flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (Exception e){
                String messege = e.getMessage();
                response.getOutputStream().print(messege);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                writeLock.unlock();
            }
        }
    }

    private void viewSheet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        else {
            String sheetName = request.getParameter("sheetName");
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            Lock readLock = sheetManger.getReadLock(sheetName);
            try {
                readLock.lock();
                Logic sheet = sheetManger.getSheet(sheetName);
                if(sheet.getOwner().equals(usernameFromSession) || sheet.getPermission(usernameFromSession) != null){
                    SheetDTO mainSheet = sheet.getSheet();
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(Coordinate.class, new CoordinateAdapter())
                            .create();
                    String json = gson.toJson(mainSheet);
                    response.setContentType("application/json");
                    response.getWriter().write(json);
                    response.getWriter().flush();
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
                response.getOutputStream().print(e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } finally {
                readLock.unlock();
            }
        }
    }
}
