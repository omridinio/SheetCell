
import methods.Menu;
import body.impl.ImplCell;
import body.Cell;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Cell implCell = new ImplCell("A3");
        implCell.setOriginalValue("4");
        implCell.setEffectivelValue("A4 + A5");
        implCell.setLastVersionUpdate(3);

        Scanner scanner = new Scanner(System.in);
        Menu mainMenu = new Menu();
        mainMenu.printMenu();
        int option = scanner.nextInt();

        //mainMenu.displaySingleCell(cell);

    }
}
