
import body.Logic;
import body.impl.ImplLogic;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import menu.impl.MainMenu;
import body.impl.ImplCell;
import body.Cell;

import java.util.InputMismatchException;
import java.util.Scanner;


import static menu.impl.MainMenu.READFILE;

public class Main extends Application {



    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//       Logic logic = new ImplLogic();
//
//        boolean loaded = false;
//        while (true) {
//            MainMenu.printMenu();
//            Scanner scanner = new Scanner(System.in);
//            int option;
//            while (true){
//                    try {
//                        String input = scanner.nextLine();
//                        option = Integer.parseInt(input.trim());
//                        MainMenu choose = MainMenu.parser(option);
//                        if (option != 1 && option !=8 && option != 7 && !loaded){
//                           System.out.println("Please upload Sheet first.(Press (1 or 7))");
//                        }
//                        else{
//                            choose.invoke(logic);
//                            loaded = !logic.getMainSheet().isEmpty();
//                            break;
//                        }
//                    }
//                    catch (NumberFormatException e) {
//                        System.out.println("ERROR! Please enter a number:");
//                    }
//                    catch(IllegalArgumentException e){
//                        System.out.println(e.getMessage());
//                    }
//                    catch (ClassCastException e) {
//                        System.out.println("ERROR! Please enter Values that match to the function:");
//                    }
//                    catch (Exception e){
//                        System.out.println(e.getMessage());
//                    }
//            }
//        }


    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Test");
        // Load the FXML file and set the controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Shitsel.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
