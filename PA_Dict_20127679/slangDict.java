//package PA_Dict_20127679;

import java.util.*;
import java.io.*;

public class slangDict {
    // private static Scanner input = new Scanner(System.in);
    
    private static HashMap<String,ArrayList<String>> dictionary = new HashMap<>();
    //private static HashMap<String,ArrayList<String>> history = new HashMap<>();

    public slangDict(){
        inputDict("slang.txt");
        viewDictConsole();
    }

    //input file dict
    public void inputDict(String filename){
        dictionary.clear();
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line!=null){
                String[] splitLine = line.split("`");
                    //update dictionary here 
                String[] splitDefinition = null;
                ArrayList<String> definition = null;
                if (splitLine.length>1){
                    splitDefinition = splitLine[1].split("\\| ");
                    definition = new ArrayList<>(Arrays.asList(splitDefinition));
                }
                dictionary.put(splitLine[0], definition); 
                line = reader.readLine();
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    //output file dict
    public void outputDict(String filename) throws IOException{
        String s;
        FileWriter fw;
        try{
            fw = new FileWriter(filename);
        }
        catch(IOException e){
            System.out.println("Error opening file");
            return;
        }
        for (String word:dictionary.keySet()){
            s=word+"`";
            ArrayList<String> a = dictionary.get(word);
            for (String d : a){
                s+=d+"| ";
            }
            s+="\n";
            fw.write(s);
        }
        fw.close();
        System.out.println("Output Successfully!");
    }

        //need to save history?

    //search slang word
    public void searchWord(String word){
        for (String str:dictionary.keySet()){
            if (word.equals(str)){
                String s=str+"`";
                ArrayList<String> a = dictionary.get(str);
                for (String d : a){
                    s+=d+"| ";
                }
                System.out.println(s);
                return;
            }
        }
        System.out.println("This slang is not in dictionary (UmU)...");
    }

    //search definition
    public void searchDefinition(String[] definition){

    }

    //show search history 
    public void viewSearchHistory(){

    }

    //add slang --> check condition: overwrite/duplicate
    public void addSlang(){

    }

    //edit slang
    public void editSlang(){

    }

    //delete slang --> need confirm
    public void deleteSlang(){

    }

    //reset slang list
    public void resetDict(){

    }

    //random slang (On this day slang word)
    public void randomSlang(){

    }

    //minigame: 1 slang 4 definition options
    public void wordMinigame(){

    }

    //minigame: 1 def 4 slang
    public void definitionMinigame(){

    }

        //additional methods for support/debug
    LinkedHashMap<String,ArrayList<String>> sortedDict = new LinkedHashMap<>();
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
            String s=word+"`";
            ArrayList<String> a = dictionary.get(word);
            if (a!=null){
                for (String d : a){
                    s+=d+"| ";
                }
            }
            System.out.println(s);
        }
        System.out.println();
    }

}
