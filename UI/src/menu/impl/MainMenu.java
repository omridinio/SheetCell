package menu.impl;

import body.Logic;
import menu.Menu;

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
        }

        void display(){
            System.out.println("Please enter the cell identifier (e.g., A4):");
        }
    },
    UPDATECELL{
        @Override
        public void invoke(Logic logic) {
            display();
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
            default:
                exit(1);
        }
        return null;
    }
}
