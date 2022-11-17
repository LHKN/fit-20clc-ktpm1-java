package PA_Dict_20127679;
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
            System.out.println("Enter 10 to play minigame! Guess the definition of the slang;");
            System.out.println("Enter 11 to play minigame! Guess the slang.");

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

                    break;
                }
                case 3:
                {

                    break;
                }
                case 4:
                {

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
                case 12:
                {
                    // File myFile = new File(filename);
                    // sd.inputDict(myFile.getCanonicalPath());
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