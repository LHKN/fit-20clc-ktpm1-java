//package PA_Dict_20127679;

import java.util.*;
import java.io.*;

public class slangDict {
    private static String fi = "slang.txt";

    private static Scanner input = new Scanner(System.in);
    private static Random rand = new Random();
    
    private static HashMap<String,ArrayList<String>> dictionary = new HashMap<>();
    private static HashMap<String,ArrayList<String>> history = new HashMap<>();

    public slangDict(){
        inputDict(fi);
        sortWord();
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
                s+=d+"\n ";
            }
            s+="\n";
            fw.write(s);
        }
        fw.close();
        System.out.println("Output Successfully!");
    }

        //need to save history?

    //search by slang word
    public boolean searchWord(String word){
        for (String str:dictionary.keySet()){
            if (word.equals(str)){
                System.out.println("    Definition [OwO ]   :");
                String s="";
                ArrayList<String> a = dictionary.get(str);
                for (String d : a){
                    s+=d+"\n";
                }
                System.out.println(s);

                history.put(str, dictionary.get(str));
                return true;
            }
        }
        return false;
    }

    //search by definition
    public void searchDefinition(String definition){
        boolean check = false;
        HashMap<String,ArrayList<String>> result = new HashMap<>();

        for (Map.Entry<String,ArrayList<String>> a:dictionary.entrySet()){
            if (a.getValue()!=null){
                for (String d : a.getValue()){
                    if (definition.equals(d)){
                        check = true;
                        result.put(a.getKey(), a.getValue());
                    }   
                }
            }
        }
        if(check){
            System.out.println("    Slang Result [OwO ]   :");
            for (Map.Entry<String,ArrayList<String>> a:result.entrySet()){
                System.out.println(a.getKey()+"\n");
                history.put(a.getKey(), a.getValue());
            }
        }
        else{
            System.out.println("We can not find the definition in this dictionary (UmU)...\n");
        } 
    }

    //show search history 
    public void viewSearchHistory(){
        System.out.println("    Search History [OwO ]   :");
        if (history.keySet().size() == 0){
            System.out.println("You haven't searched any slangs (ehe)>[UwU ]\n");
            return;
        }
        for (String word:history.keySet()){
            String s=word+"`";
            ArrayList<String> a = history.get(word);
            if (a!=null){
                for (String d : a){
                    s+=d+"| ";
                }
            }
            System.out.println(s);
        }
        System.out.println();
    }

    //add slang --> check condition: overwrite/duplicate
    public void addSlang(String word){
        if(searchWord(word)){ //exist
            System.out.println("The slang word already exists!! {-_- }");
            System.out.println("Enter 1 to overwrite; Enter 2 to add to definition: ");
            int opt = input.nextInt();
            switch(opt){
                case 2: 
                {
                    System.out.print("Enter new definition: ");
                    String nw = input.nextLine();
                    nw = input.nextLine();
                    dictionary.get(word).add(nw);
                    dictionary.put(word,dictionary.get(word));
                    System.out.println("Updated!! {~_~ } \n");
                    return;
                }
                case 1:
                default:
                {
                    System.out.print("Enter new definition: ");
                    String nw = input.nextLine();
                    nw = input.nextLine();
                    String[] nl = new String[]{nw};
                    dictionary.put(word,new ArrayList<String>(Arrays.asList(nl)));
                    System.out.println("Updated!! {~_~ } \n");
                    return;
                }
            }
        }
        System.out.print("Enter new definition: ");
        String nw = input.nextLine();
        String[] nl = new String[]{nw};
        dictionary.put(word,new ArrayList<String>(Arrays.asList(nl)));
        System.out.println("Updated!! {~_~ } \n");
    }

    //edit slang
    public void editSlang(String word){
        if(searchWord(word)){
            while(true){
                System.out.println("What do you want to edit? Enter 1 to replace word; Enter 2 to replace definition; Enter 3 to add to definition: ");   
                int opt = input.nextInt();
                switch(opt){
                    case 1:
                    {
                        System.out.print("Enter new slang word: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();
                        if(searchWord(nw)){
                            System.out.println("The slang word already exists!! {-_- } \n");
                            return;
                        }
                        else{
                            dictionary.put(nw,dictionary.get(word));
                            dictionary.remove(word);
                            System.out.println("Updated!! {~_~ } \n");
                            return;
                        }
                    }
                    case 2:
                    {
                        if(dictionary.get(word).size()>1){
                            System.out.println("There are "+String.valueOf(dictionary.get(word).size())+" definitions. Enter number of the definition to replace (Out of range number will replace all definitions):");
                            int n = input.nextInt();
                            
                            if(dictionary.get(word).size()>=n){
                                System.out.print("Enter new definition: ");
                                String nw = input.nextLine();
                                nw = input.nextLine();
                                dictionary.get(word).set(n-1, nw);
                                System.out.println("Updated!! {~_~ } \n");
                                return;
                            }
                            // else{
                            //     System.out.print("Invalid option!! {-_- } ");
                            //     return;
                            // }
                        }
                        System.out.print("Enter new definition: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();
                        String[] nl = new String[]{nw};
                        dictionary.put(word,new ArrayList<String>(Arrays.asList(nl)));
                        System.out.println("Updated!! {~_~ } \n");
                        return;
                    }
                    case 3:
                    {
                        System.out.print("Enter new definition: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();
                        dictionary.get(word).add(nw);
                        dictionary.put(word,dictionary.get(word));
                        System.out.println("Updated!! {~_~ } \n");
                        return;
                    }
                    default:
                    System.out.println("Option unvailable. Choose again!"); 
                }
            }
        }
        else{
            System.out.println("This slang is not in this dictionary (UmU)...\n");
        }
    }

    //delete slang --> need confirm
    public void deleteSlang(String word){
        if(searchWord(word)){
            System.out.println("Do you want to delete this slang? Enter 1 for Yes, other numbers for No: ");   
            int opt = input.nextInt();
            if (opt!=1){
                return;
            } 
            dictionary.remove(word);

            //sortedDict.remove(word);
            System.out.println("Slang is deleted! <OAO >\n");   
        }
        else{
            System.out.println("This slang is not in this dictionary (UmU)...\n");
        }
    }

    //reset slang list
    public void resetDict(){
        System.out.println("Do you want to reset the dictionary? Enter 1 for Yes, other numbers for No: ");   
        int opt = input.nextInt();
        if (opt!=1){
            return;
        } 
        dictionary.clear();
        //sortedDict.clear(); 
        System.out.println("List is reset!! <0A0 >\n");   
    }

    //random slang (On this day slang word)
    public void randomSlang(){
        int len = dictionary.keySet().size();
        int idx = rand.nextInt(len) - 1;

        System.out.println("    On this day slang word (^o^)/   :");
        Object[] set = dictionary.keySet().toArray();

        System.out.println(" Slang: "+set[idx]);
        System.out.println(" Definition: ");
        String s="";
        ArrayList<String> a = dictionary.get(set[idx]);
        for (String d : a){
            s+=d+"\n";
        }
        System.out.println(s);
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
        sortedDict.clear();
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
        if (sortedDict.keySet().size() == 0){
            System.out.println("There is no slangs in this dictionary... <0A0 >\n");
            return;
        }
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