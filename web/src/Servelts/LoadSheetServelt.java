package Servelts;

import Mangger.RequestPermissonManager;
import Mangger.SheetManger;
import Utils.Constants;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import body.Logic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.impl.AproveRequest;
import dto.impl.PermissionRequest;
import dto.impl.SheetBasicData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.xml.bind.JAXBException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;


@WebServlet(name = "LoadSheetServelt", urlPatterns = {Constants.LOAD_SHEET, Constants.REFRESH_SHEET, Constants.PERMISSON_REQUEST, Constants.PERMISSON_REFRESH, Constants.PERMISSION_OWNER, Constants.PERMISSON_APPROVE})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetServelt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.LOAD_SHEET:
                loadSheet(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.REFRESH_SHEET:
                refreshSheet(request, response);
                break;
            case Constants.PERMISSON_REQUEST:
                permissioRequest(request, response);
                break;
            case Constants.PERMISSON_REFRESH:
                refreshPermisson(request, response);
                break;
            case Constants.PERMISSION_OWNER:
                getPermissionOwner(request, response);
                break;
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        switch (request.getServletPath()) {
            case Constants.PERMISSON_APPROVE:
                permissionApprove(request, response);
                break;
        }
    }

    private void permissionApprove(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        synchronized (this) {
           int requestId = Integer.parseInt(request.getParameter("requestId"));
            String newStatus = request.getParameter("status");
            RequestPermissonManager requestPermissonManager = ServeltUtils.getPermissionRequestManager(getServletContext());
            try {
                PermissionRequest perRequest = requestPermissonManager.deleteRequest(usernameFromSession, requestId, newStatus);
                if (perRequest != null) {
                    if (newStatus.equals("approved")) {
                        SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
                        Logic sheet = sheetManger.getSheet(perRequest.getSheetName());
                        sheet.addPermission(perRequest.getUsername(), perRequest.getPermission());
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void getPermissionOwner(HttpServletRequest request, HttpServletResponse response) {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        synchronized (this) {
            RequestPermissonManager requestPermissonManager = ServeltUtils.getPermissionRequestManager(getServletContext());
            List<PermissionRequest> requests = requestPermissonManager.getAllRequestByOwner(usernameFromSession);
            Gson gson = new Gson();
            String json = gson.toJson(requests);
            response.setContentType("application/json");
            try {
                response.getWriter().write(json);
                response.getWriter().flush();
            } catch (IOException e) {
            }
        }
    }

    private void refreshPermisson(HttpServletRequest request, HttpServletResponse response) {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String sheetName = request.getParameter("sheetName");
        RequestPermissonManager requestPermissonManager = ServeltUtils.getPermissionRequestManager(getServletContext());
        List<PermissionRequest> requests = requestPermissonManager.getAllRequestBySheet(sheetName);
        Gson gson = new Gson();
        String json = gson.toJson(requests);
        response.setContentType("application/json");
        try {
            response.getWriter().write(json);
            response.getWriter().flush();
        } catch (IOException e) { }
    }

    private void permissioRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("Error: No logged in user");
        }
        else{
            synchronized (this) {
                RequestPermissonManager requestPermissonManager = ServeltUtils.getPermissionRequestManager(getServletContext());
                PermissionRequest permissionRequest = ServeltUtils.createPermissionRequest(request);
                if (requestPermissonManager.isRequestExist(permissionRequest)) {
                    permissionRequest.delete();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getOutputStream().print("Error: Request already exist");
                    return;
                }
                requestPermissonManager.addRequest(permissionRequest);
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

    }

    private void refreshSheet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if(usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("Error: No logged in user");
            return;
        }
        int index = Integer.parseInt(request.getParameter("index"));
        SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
        //List<SheetBasicData> sheetsName = sheetManger.getSheetBasicData(usernameFromSession);
        List<SheetBasicData> sheetsName = sheetManger.getSheetBasicData(usernameFromSession, index);
        Gson gson = new Gson();
        String json = gson.toJson(sheetsName);
        response.setContentType("application/json");
        response.getWriter().write(json);
        response.getWriter().flush();
    }

    private void loadSheet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/plain;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUserNameFromSession(request);
        if (usernameFromSession == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print("Error: No logged in user");
        }
        else {
            Part filePart = request.getPart("file");
            InputStream fileContent = filePart.getInputStream();
            SheetManger sheetManger = ServeltUtils.getSheetManger(getServletContext());
            try {
                Logic newSheet = sheetManger.createNewSheet(fileContent, usernameFromSession);
                String sheetName = newSheet.getSheet().getSheetName();
                synchronized (this) {
                    if(sheetManger.isSheetExist(sheetName)){
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print("Error: Sheet already exist");
                    }
                    else{
                        sheetManger.addSheet(sheetName, newSheet);
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.getOutputStream().print("Sheet " + sheetName + " added successfully");
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getOutputStream().print("Error: " + e.getMessage());
            }

        }
    }

}



