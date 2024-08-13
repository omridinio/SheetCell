
import body.Logic;
import body.impl.ImplLogic;
import menu.impl.MainMenu;
import body.impl.ImplCell;
import body.Cell;

import java.util.Scanner;

public class Main {


    public static void main(String[] args) {
        Cell implCell = new ImplCell("A3");
        implCell.setLastVersionUpdate(3);

        Logic logic = new ImplLogic();
        while (true){
            MainMenu.printMenu();
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            MainMenu choose = MainMenu.parser(option);
            choose.invoke(logic);
        }

        //mainMenu.displaySingleCell(cell);

    }
}
