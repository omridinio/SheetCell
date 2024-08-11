package methods;

import body.Cell;

import java.util.Scanner;

public class Menu {


    public void printMenu(){
         System.out.println("1) Read File");
         System.out.println("2) Display Spreadsheet");
         System.out.println("3) Display Single Cell");
         System.out.println("4) Update Single Cell");
         System.out.println("5) Display Versions");
     }

     public void displaySingleCell(Cell cell, Boolean fullInfo){
         System.out.println("Cell Identity " + cell.getId());
         System.out.println("Original Value of the Cell " + cell.getOriginalValue());
         System.out.println("Effective Value of the Cell " + cell.getEffectivelValue());
         if(fullInfo){
             System.out.println("Last Version in which it was Modified " + cell.getLastVersionUpdate());
             System.out.println("List of Cell it Depends On " + cell.getCellsDependsOnThem());
             System.out.println("List of Cell it Affects ");
         }
     }
}
