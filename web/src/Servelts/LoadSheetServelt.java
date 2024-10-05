package Servelts;

import Mangger.SheetManger;
import Utils.ServeltUtils;
import Utils.SessionUtils;
import body.Logic;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;

@WebServlet(name = "LoadSheetServelt", urlPatterns = {"/loadSheet"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 5 * 5)
public class LoadSheetServelt extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
                Logic newSheet = sheetManger.createNewSheet(fileContent);
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



