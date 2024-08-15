
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
        while (true) {
            boolean success = false;
            MainMenu.printMenu();
            Scanner scanner = new Scanner(System.in);
            int option = scanner.nextInt();
            MainMenu choose = MainMenu.parser(option);
            while (!success) {
                try {
                    choose.invoke(logic);
                    success = true;
               } catch (ClassCastException e) {
                    System.out.println("ERROR! Please enter Values that match to the function:");
               }
            }

        }
    }
}
