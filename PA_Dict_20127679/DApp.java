//package PA_Dict_20127679;
import java.io.*;
import java.util.*;

public class DApp{
    //main
    //use swingUI/Console
    //draw cmd from slangDict

    private static String filename = "slang.txt";
    //console
    public static void main(String args[]) throws IOException{
        Scanner input = new Scanner(System.in);
        slangDict sd = new slangDict();
        while(true){
            System.out.println("Enter 1 to end the program;");

            System.out.println("Enter 2 to search a slang's definition using slang;");
            System.out.println("Enter 3 to search a slang using slang's definition;");
            System.out.println("Enter 4 to view search history;");
            System.out.println("Enter 5 to add a slang;");
            System.out.println("Enter 6 to edit a slang;");
            System.out.println("Enter 7 to delete a slang;");
            System.out.println("Enter 8 to reset slang list;");
            System.out.println("Enter 9 to randomize a slang (On this day slang word);");
            System.out.println("Enter 10 to play minigame ( ._.)! Guess the definition of the slang;");
            System.out.println("Enter 11 to play minigame (._. )! Guess the slang.");

            System.out.println("Enter 12 to view dictionary.");

            int option = input.nextInt();
            System.out.println();

            switch(option){
                case 1: //end program
                {
                    input.close();
                    System.exit(0);
                    break;
                }
                case 2:
                {
                    boolean check = true;
                    while(check){
                        System.out.print("Enter slang: ");
                        String word = input.nextLine();
                        word = input.nextLine();
                        sd.searchWord(word);

                        System.out.println("Enter another slang? Enter 1 for Yes, other numbers for No: ");
                        int opt = input.nextInt();
                        if (opt!=1) check=!check;
                        System.out.println();
                    }
                    break;
                }
                case 3:
                {
                    boolean check = true;
                    while(check){
                        System.out.print("Enter slang's definition: ");
                        String word = input.nextLine();
                        word = input.nextLine();
                        sd.searchDefinition(word);

                        System.out.println("Enter another slang's definition? Enter 1 for Yes, other numbers for No: ");
                        int opt = input.nextInt();
                        if (opt!=1) check=!check;
                        System.out.println();
                    }
                    break;
                }
                case 4:
                {
                    sd.viewSearchHistory();
                    break;
                }
                case 5:
                {

                    break;
                }
                case 6:
                {

                    break;
                }
                case 7:
                {

                    break;
                }
                case 8: 
                {
                    break;
                }
                case 9:
                {
                    break;
                }
                case 10:
                {
                    break;
                }
                case 11:
                {
                    break;
                }
                case 12:
                {
                    sd.inputDict(filename);
                    sd.viewDictConsole();
                    break;
                }
                default:
                System.out.println("Option unvailable. Choose again!");       
            }
        }
    }
}