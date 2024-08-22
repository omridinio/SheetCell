package body;

import dto.SheetDTO;
import dto.impl. CellDTO;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Logic {
    CellDTO getCell(String cellID);
    void updateCell(String cellId, String value);
    SheetDTO getSheet();
    void creatNewSheet(String path)throws JAXBException, FileNotFoundException, IOException;
}
