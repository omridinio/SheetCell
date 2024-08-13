package menu.impl;

import body.Cell;
import body.Logic;
import dto.CellDTO;
import menu.Menu;

import java.util.Scanner;

import static java.lang.System.exit;

public enum MainMenu implements Menu {
    READFILE{
        @Override
        public void invoke(Logic logic) {
            display();

        }
        void display(){
            System.out.println("Please enter the full path to the XML file you want to load: ");
            System.out.println("Example for Windows: C:\\path\\to\\your\\file.xml");
        }
    },
    DISPLAYSPREADSHEET{
        @Override
        public void invoke(Logic logic) {
            display();
        }

        void display(){
            System.out.println("SPREADSHEET ");
        }
    },
    DISPLAYCELL{
        @Override
        public void invoke(Logic logic) {
            display();
            Scanner scanner = new Scanner(System.in);
            String enterdCell = scanner.next();
            printCell(logic.getCell(enterdCell));
        }

        void display(){
            System.out.println("Please enter the cell identifier (e.g., A4):");
        }
    },
    UPDATECELL{
        @Override
        public void invoke(Logic logic) {
            display();
            Scanner scanner = new Scanner(System.in);
            String enterdCell = scanner.next();
            printCell(logic.getCell(enterdCell));
            System.out.println("Please enter the new Value:");
            scanner.nextLine();
            String enterdValue = scanner.nextLine();
            logic.updateCell(enterdCell, enterdValue);
            printCell(logic.getCell(enterdCell));

        }

        void display(){
            System.out.println("Please enter the cell identifier (e.g. A4):");
        }
    },
    DISPLAYVERSION{
        @Override
        public void invoke(Logic logic) {
            display();

        }
        private void display(){
            System.out.print("Please enter the version number to preview: ");
        }
    };

    public static void printMenu(){
        System.out.println("1) Read File");
        System.out.println("2) Display Spreadsheet");
        System.out.println("3) Display Single Cell");
        System.out.println("4) Update Single Cell");
        System.out.println("5) Display Versions");
        System.out.println("6) Exit");
    }

    public static MainMenu parser (int option){
        switch(option){
            case 1:
                return READFILE;
            case 2:
                return DISPLAYSPREADSHEET;
            case 3:
                return DISPLAYCELL;
            case 4:
                return UPDATECELL;
            case 5:
                return DISPLAYVERSION;
            case 6:
                exit(0);
            default:
                throw new IllegalArgumentException("Invalid option was pressed. Please try again.");
        }
    }
    public static void printCell (CellDTO cell){
        System.out.println("Name: " + cell.getId());
        System.out.println("Original value: " + cell.getOriginalValue());
        try{
            System.out.println("Effective value: " + cell.getEffectiveValue());
        }catch(NullPointerException e){
            System.out.println("Empty effective value");
        }
    }
}
