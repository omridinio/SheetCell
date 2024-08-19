package menu.impl;

import body.Coordinate;
import dto.SheetDTO;

public interface UI {
    void printSheet(SheetDTO currShee);
    void updateCell(Coordinate cellId, String value);
    void getCell(Coordinate coordinate);
}
