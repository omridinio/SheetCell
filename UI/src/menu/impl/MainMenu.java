package menu.impl;

import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import menu.Menu;

import javax.swing.*;
import java.util.Scanner;

import static java.lang.System.exit;

public enum MainMenu implements Menu {
    READFILE{
        @Override
        public void invoke(Logic logic) {
            display();
            logic.sortG();

        }
        void display(){
            System.out.println("Please enter the full path to the XML file you want to load: ");
            System.out.println("(Example for Windows: C:\\path\\to\\your\\file.xml)\n");
        }
    },
    DISPLAYSPREADSHEET{
        @Override
        public void invoke(Logic logic) {
            display();
            printSheet(logic.getSheet());
        }
        //TODO A3 prints in B3 check why!!
        public void printSheet(SheetDTO currSheet) {
            String whiteSpace = makeWidth(currSheet.getWidth());
            System.out.println("Sheet version: " + currSheet.getVersion() + System.lineSeparator() + "Sheet name: " + currSheet.getSheetName());
            System.out.print("  "); // Leading space for row numbers
            for (int i = 0; i < currSheet.getColumnCount(); i++) {
                System.out.print((char) ('A' + i) + whiteSpace);
            }
            System.out.println();

            // Print the rows with numbers and placeholders
            for (int i = 1; i <= currSheet.getRowCount(); i++) {
                System.out.print(i + "|"); // Print the row number
                for (int j = 0; j < currSheet.getColumnCount(); j++) {
                    Coordinate currCoord = new CoordinateImpl(i,j);
                    EffectiveValue currCell = currSheet.getEfectivevalueCell(currCoord);
                    if(currCell != null){
                        int cellWidth = currCell.getValue().toString().length();
                        String tempWhiteSpace = makeWidth(currSheet.getWidth() - cellWidth);
                        System.out.print(currCell.getValue() + tempWhiteSpace + "|");
                    }
                    else {
                        System.out.print(whiteSpace + "|"); // Placeholder for cell content
                    }
                }
                System.out.println();
                for(int j = 0; j < currSheet.getThickness() - 1; j++){
                    System.out.print(" |");
                    for (int K = 0; K < currSheet.getColumnCount(); K++) {
                        System.out.print(whiteSpace + "|");
                    }
                    System.out.println();
                }

            }
        }

        private String makeWidth(int width){
            String res = " ";
            for (int i = 0; i < width; i++) {
                res += " ";
            }
            return res;
        }

        void display(){
            System.out.println("SPREADSHEET \n");
        }
    },
    DISPLAYCELL{
        @Override
        public void invoke(Logic logic) {
            display();
            Scanner scanner = new Scanner(System.in);
            String enterdCell = scanner.next();

            printCell(logic.getCell(null));
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
            String enterdCell = null;
            while(true) {
                enterdCell = scanner.next();
                enterdCell = validInputCell(enterdCell);
                try{
                    printCell(logic.getCell(enterdCell));
                    break;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Please enter the cell identifier (e.g., A4):");
            }
            System.out.println("Please enter the new Value:");
            scanner.nextLine();
            while(true){
                String enterdValue = scanner.nextLine();
                try {
                    logic.updateCell(enterdCell, enterdValue);
                    printCell(logic.getCell(enterdCell));
                    break;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Please enter the new Value:");
            }
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
            System.out.print("Please enter the version number to preview:");
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

    //TODO: maybe fowrod to logic
    String validInputCell(String input){
        while(true) {
            if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {
                String temp = input.substring(1);
                try {
                    if(Integer.parseInt(temp) > 0) {
                        break;
                    }
                } catch (NumberFormatException e) { }
            }
            System.out.println("Invalid input, please enter a valid cell identifier (e.g., A4):");
            Scanner scanner = new Scanner(System.in);
            input = scanner.next();
        }
        return input;
    }
}
