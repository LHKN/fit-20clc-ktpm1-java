package PA_Dict_20127679;

import java.util.*;
import java.io.*;

public class slangDict {
    // private static Scanner input = new Scanner(System.in);
    
    private HashMap<String,String[]> dictionary = new HashMap<>();
    private HashMap<String,String[]> history = new HashMap<>();
    //put defition as slangCard to make multiple duplicate slangs?

    //input file dict
    void inputDict(String filename){
        dictionary.clear();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while(line!=null){
                String[] splitLine = line.split("'");
                //update dictionary here 
                String[] definition = splitLine[0].split(" ");
                dictionary.put(splitLine[1], definition); 
                line = reader.readLine();
            }
            reader.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    //output file dict
    void outputDict(String filename) throws IOException{
        String s;
        FileWriter fw;
        try{
            fw = new FileWriter(filename);
        }
        catch(IOException e){
            System.out.println("Error opening file");
            //swingUI
            return;
        }
        for (String word:dictionary.keySet()){
            s=word+"'"+dictionary.get(word)+"/n";
            fw.write(s);
        }
        fw.close();
        System.out.println("Output Successfully!");
    }

        //need to save history?

    //search slang word

    //search definition

    //show search history 
    
    //add slang --> check condition: overwrite/duplicate

    //edit slang

    //delete slang --> need confirm

    //reset slang list

    //random slang (On this day slang word)

    //minigame: 1 slang 4 definition options

    //minigame: 1 def 4 slang

    //additional methods for support/debug
    LinkedHashMap<String,String[]> sortedDict = new LinkedHashMap<>();
    class wordComparator implements Comparator<String>{
        public int compare(String s1, String s2){
            return s1.compareTo(s2);
        }
    }

    //sort dictionary
    public void sortWord(){
        wordComparator wc = new wordComparator();
        ArrayList<String> wordList = new ArrayList<>();
        for (String s: dictionary.keySet()) {
            wordList.add(s);
        }
        Collections.sort(wordList,wc);

        for (String s1: wordList) {
            for (String s2: dictionary.keySet()) {
                if (s1.equals(s2)) {
                    sortedDict.put(s1, dictionary.get(s1));
                }
            }
        }
    }

    //view dictionary on console
    public void viewDictConsole(){
        sortWord();
        for (String word:sortedDict.keySet()){
            System.out.println(word+"'"+sortedDict.get(word)+"/n");
        }
    }

}
