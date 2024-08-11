
import builders.Menu;
import builders.impl.Cell;

public class Main {


    public static void main(String[] args) {
        Cell cell = new Cell();
        cell.setId("A3");
        cell.setOriginalValue("4");
        cell.setEffectivelValue("A4 + A5");
        cell.setLastVersionUpdate(3);

        Menu mainMenu = new Menu();
        mainMenu.printMenu();
        //mainMenu.displaySingleCell(cell);

    }
}
