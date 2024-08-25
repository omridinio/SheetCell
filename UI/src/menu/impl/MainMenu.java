package menu.impl;

import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import jaxb.generated.STLSheet;
import menu.Menu;

import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public enum MainMenu implements Menu {
    READFILE{
        @Override
        public void invoke(Logic logic) {
            boolean success = false;
            while (!success) {
                display();
                Scanner scanner = new Scanner(System.in);
                String enterdPath = scanner.nextLine();
                if(enterdPath.equals("0")) {
                    break;
                }
                try {
                    logic.creatNewSheet(enterdPath);
                    success = true;
                } catch (ClassCastException e) {
                    System.out.println("ERROR! in the file, one of the cells has a value that does not match the function");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        void display(){
            System.out.println("Please enter the full path to the XML file you want to load: ");
            System.out.println("(Example for Windows: C:\\path\\to\\your\\file.xml)");
            System.out.println("To exit to main menu please enter '0' ");        }
    },
    DISPLAYSPREADSHEET{
        @Override
        public void invoke(Logic logic) {
            printSheet(logic.getSheet());
        }

//        public void printSheet(SheetDTO currSheet) {
//            String whiteSpace = makeWidth(currSheet.getWidth());
//            System.out.println("Sheet version: " + currSheet.getVersion() + System.lineSeparator() + "Sheet name: " + currSheet.getSheetName());
//            System.out.print(makeWidth(howManyDigits(currSheet.getRowCount())) + " "); // Leading space for row numbers
//            for (int i = 0; i < currSheet.getColumnCount(); i++) {
//                System.out.print((char) ('A' + i) + whiteSpace);
//            }
//            System.out.println();
//
//            // Print the rows with numbers and placeholders
//            for (int i = 1; i <= currSheet.getRowCount(); i++) {
//                String whiteSpaceBeforeRow = makeWidth(howManyDigits(currSheet.getRowCount()) - howManyDigits(i));
//                System.out.print(i+ whiteSpaceBeforeRow + "|"); // Print the row number
//                for (int j = 1; j <= currSheet.getColumnCount(); j++) {
//                    Coordinate currCoord = new CoordinateImpl(i,j);
//                    EffectiveValue currCell = currSheet.getEfectivevalueCell(currCoord);
//                    if(currCell != null){
//                        int cellWidth = currCell.toString().length();
//                        String tempWhiteSpace = makeWidth(currSheet.getWidth() - cellWidth);
//                        System.out.print(currCell.toString() + tempWhiteSpace + "|");
//                    }
//                    else {
//                        System.out.print(whiteSpace + "|"); // Placeholder for cell content
//                    }
//                }
//                System.out.println();
//                for(int j = 0; j < currSheet.getThickness() - 1; j++){
//                    System.out.print(makeWidth(howManyDigits(currSheet.getRowCount())) + "|");
//                    for (int K = 0; K < currSheet.getColumnCount(); K++) {
//                        System.out.print(whiteSpace + "|");
//                    }
//                    System.out.println();
//                }
//
//            }
//        }

//        private int howManyDigits(int number){
//            if (number == 0) {
//                return 1;
//            }
//            return (int) Math.log10(Math.abs(number)) + 1;
//        }
//
//        private String makeWidth(int width){
//            String res = "";
//            for (int i = 0; i < width; i++) {
//                res += " ";
//            }
//            return res;
//        }

    },
    DISPLAYCELL{
        @Override
        public void invoke(Logic logic) {
            while (true) {
                display();
                Scanner scanner = new Scanner(System.in);
                String enterdCell = scanner.nextLine();
                if (enterdCell.equals("0")) {
                    break;
                }
                try{ printCell(logic.getCell(enterdCell.toUpperCase()), false);
                    break;
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        }

        void display(){
            System.out.println("Please enter the cell identifier (e.g., A4):");
            System.out.println("To exit to main menu please enter '0' ");
        }
    },
    UPDATECELL{
        @Override
        public void invoke(Logic logic) {
            display();
            Scanner scanner = new Scanner(System.in);
            String enterdCell = null;
            boolean success = false;
            while(true) {
                enterdCell = scanner.nextLine();
                if (enterdCell.equals("0")) {
                    break;
                }
                enterdCell = validInputCell(enterdCell.toUpperCase());
                try{
                    printCell(logic.getCell(enterdCell),true);
                    success = true;
                    break;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Please enter the cell identifier (e.g., A4):");
            }

            while(success){
                System.out.println("Please enter the new Value:");
                String enterdValue = scanner.nextLine();
                try {
                    logic.updateCell(enterdCell, enterdValue);
                    printCell(logic.getCell(enterdCell),true);
                    break;
                } catch (ClassCastException  e) {
                    System.out.println("ERROR! Please enter Values that match to the function:");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if(success){
                DISPLAYSPREADSHEET.invoke(logic);
            }
        }

        void display(){
            System.out.println("Please enter the cell identifier (e.g. A4):");
            System.out.println("To exit to main menu please enter '0' ");
        }

    },
    DISPLAYVERSION{
        @Override
        public void invoke(Logic logic) {
            List<Integer> CellsPerVersion = logic.getNumberOfUpdatePerVersion();
            System.out.println("version  |  Number of cells updated");
            for (int i = 0; i < CellsPerVersion.size(); i++) {
                System.out.println(i + 1 + "        |  " + CellsPerVersion.get(i));
            }
            System.out.println("To exit to main menu please enter '0' ");
            int option = 0;
            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    display();
                    String input = scanner.nextLine();
                    option = Integer.parseInt(input.trim());
                    if (option == 0) {
                        break;
                    }
                    if(option > 0 && option <= CellsPerVersion.size()){
                        SheetDTO sheet = logic.getSheetbyVersion(option - 1);
                        printSheet(sheet);
                        break;

                    }
                    System.out.println("Invalid input, please enter a valid version number:");

                } catch (NumberFormatException e) {
                    System.out.println("ERROR! Please enter a number only");
                }
            }

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
                throw new IllegalArgumentException("Invalid option was pressed, only 1-6 numbers. Please try again.");
        }
    }

    public void printSheet(SheetDTO currSheet) {
        String whiteSpace = makeWidth(currSheet.getWidth());
        System.out.println("Sheet version: " + currSheet.getVersion() + System.lineSeparator() + "Sheet name: " + currSheet.getSheetName());
        System.out.print(makeWidth(howManyDigits(currSheet.getRowCount())) + " "); // Leading space for row numbers
        for (int i = 0; i < currSheet.getColumnCount(); i++) {
            System.out.print((char) ('A' + i) + whiteSpace);
        }
        System.out.println();

        // Print the rows with numbers and placeholders
        for (int i = 1; i <= currSheet.getRowCount(); i++) {
            String whiteSpaceBeforeRow = makeWidth(howManyDigits(currSheet.getRowCount()) - howManyDigits(i));
            System.out.print(i+ whiteSpaceBeforeRow + "|"); // Print the row number
            for (int j = 1; j <= currSheet.getColumnCount(); j++) {
                Coordinate currCoord = new CoordinateImpl(i,j);
                EffectiveValue currCell = currSheet.getEfectivevalueCell(currCoord);
                if(currCell != null){
                    int cellWidth = currCell.toString().length();
                    String tempWhiteSpace = makeWidth(currSheet.getWidth() - cellWidth);
                    System.out.print(currCell.toString() + tempWhiteSpace + "|");
                }
                else {
                    System.out.print(whiteSpace + "|"); // Placeholder for cell content
                }
            }
            System.out.println();
            for(int j = 0; j < currSheet.getThickness() - 1; j++){
                System.out.print(makeWidth(howManyDigits(currSheet.getRowCount())) + "|");
                for (int K = 0; K < currSheet.getColumnCount(); K++) {
                    System.out.print(whiteSpace + "|");
                }
                System.out.println();
            }

        }
    }

    private int howManyDigits(int number){
        if (number == 0) {
            return 1;
        }
        return (int) Math.log10(Math.abs(number)) + 1;
    }

    private String makeWidth(int width){
        String res = "";
        for (int i = 0; i < width; i++) {
            res += " ";
        }
        return res;
    }

    public static void printCell (CellDTO cell,boolean inUpdate){
        System.out.println("Name: " + cell.getId());
        System.out.println("Original value: " + cell.getOriginalValue());
        try{
            System.out.println("Effective value: " + cell.getOriginalEffectiveValue().toString());
        }catch(NullPointerException e){
            System.out.println("Empty effective value");
        }
        if(!inUpdate){
            System.out.println("Last changed version: " + cell.getLastVersionUpdate());
            System.out.println("List of cells that Depend on " + cell.getId() + ": " + cell.getCellsDependsOnHim() );
            System.out.println("List of cells that " + cell.getId() + " depend on: " + cell.getCellsDependsOnThem() );
        }
    }

    //TODO: maybe forward to logic
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
            input = scanner.nextLine();
            input = input.toUpperCase();
        }
        return input;
    }
}
