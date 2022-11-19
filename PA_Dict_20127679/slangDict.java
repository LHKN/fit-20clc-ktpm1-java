//package PA_Dict_20127679;

import java.util.*;
import java.io.*;

public class slangDict {
    private static String fi = "slang.txt";
    private static String fo = "slang2.txt";

    private static Scanner input = new Scanner(System.in);
    private static Random rand = new Random();
    
    private static HashMap<String,ArrayList<String>> dictionary = new HashMap<>();
    private static HashMap<String,ArrayList<String>> history = new HashMap<>();

    public slangDict(){
        inputDict(fi);
        try{
            outputDict(fo);

        }catch(Exception e){
            e.printStackTrace();
        }
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
        sortWord();
        String s = "";
        BufferedWriter writer;
        try{
            writer = new BufferedWriter(new FileWriter(filename));
        }
        catch(IOException e){
            System.out.println("Error opening file\n");
            return;
        }
        for (String word:sortedDict.keySet()){
            s+=word+"`";
            ArrayList<String> a = sortedDict.get(word);
            if (a!=null){
                for (String d : a){
                    s+=d+"| ";
                }
            }
            s+="\n";
            //fw.write(s);
        }
        writer.write(s);
        writer.close();
        System.out.println("Output Successfully!\n");
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
    public void addSlang(String word) throws IOException{
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
                    outputDict(fo);
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
                    outputDict(fo);
                    return;
                }
            }
        }
        System.out.print("Enter new definition: ");
        String nw = input.nextLine();
        String[] nl = new String[]{nw};
        dictionary.put(word,new ArrayList<String>(Arrays.asList(nl)));
        System.out.println("Updated!! {~_~ } \n");
        outputDict(fo);
    }

    //edit slang
    public void editSlang(String word) throws IOException{
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
                            outputDict(fo);
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
                                outputDict(fo);
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
                        outputDict(fo);
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
                        outputDict(fo);
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
    public void deleteSlang(String word) throws IOException{
        if(searchWord(word)){
            System.out.println("Do you want to delete this slang? Enter 1 for Yes, other numbers for No: ");   
            int opt = input.nextInt();
            if (opt!=1){
                return;
            } 
            dictionary.remove(word);

            //sortedDict.remove(word);
            System.out.println("Slang is deleted! <OAO >\n"); 
            outputDict(fo);  
        }
        else{
            System.out.println("This slang is not in this dictionary (UmU)...\n");
        }
    }

    //reset slang list
    public void resetDict() throws IOException{
        System.out.println("Do you want to reset the dictionary? Enter 1 for Yes, other numbers for No: ");   
        int opt = input.nextInt();
        if (opt!=1){
            return;
        } 
        dictionary.clear();
        //sortedDict.clear(); 
        System.out.println("List is reset!! <0A0 >\n");   
        outputDict(fo);
    }

    //random slang (On this day slang word)
    public String randomS(){
        int len = dictionary.keySet().size();
        int idx = rand.nextInt(len) - 1;
    
        Object[] set = dictionary.keySet().toArray();

        return String.valueOf(set[idx]);
    }

    public void randomSlang(){
        String slang = randomS();
        System.out.println("    On this day slang word (^o^)/   :");
        System.out.println(" Slang: "+String.valueOf(slang));
        System.out.println(" Definition: ");
        String s="";
        ArrayList<String> a = dictionary.get(slang);
        for (String d : a){
            s+=d+"\n";
        }
        System.out.println(s);

    }

    //minigame: 1 slang 4 definition options
    public void wordMinigame(){
        String slang = randomS();

        HashMap<String,ArrayList<String>> al = new HashMap<>();
        al.put(slang,dictionary.get(slang));

        for(int i=0;i<3;i++){
            String s = randomS();
            al.put(s,dictionary.get(s));
        }

        //display quiz
        System.out.println("    (._. ) < Slang: "+slang);
        System.out.println("    ( ._.) < Guess the definition of the slang   :");
        int count=0;
        for(ArrayList<String> a:al.values()){
            count+=1;
            String str="";
            for(String s:a){
                str+=s + "| ";
            }
            System.out.println(String.valueOf(count)+") "+str);
        }

        Object[] set = al.keySet().toArray();
        int opt = input.nextInt();
        if(slang.equals(String.valueOf(set[opt-1]))){
            System.out.println("    ( ._.) < Correct answer! Have a candy >( )< \n");
        }
        else{
            System.out.println("    (._. ) < Wrong answer!! \n");
        }
    }

    //minigame: 1 def 4 slang
    public void definitionMinigame(){
        String slang = randomS();
        ArrayList<String> definition = dictionary.get(slang);
        ArrayList<String> al = new ArrayList<>();
        al.add(slang);

        for(int i=0;i<3;i++){
            al.add(randomS());
        }

        Collections.shuffle(al);

        //display quiz
        System.out.println("    (._. ) < Definition: ");
        for(String s:definition){
            System.out.println(s);
        }
        System.out.println("    ( ._.) < Guess the slang! Enter number from 1-4:");
        int count=0;
        for(String s:al){
            count+=1;
            System.out.println(String.valueOf(count)+") "+s);
        }

        int opt = input.nextInt();
        if(al.get(opt-1).equals(slang)){
            System.out.println("    ( ._.) < Correct answer! Have a candy >( )< \n");
        }
        else{
            System.out.println("    (._. ) < Wrong answer!! \n");
        }
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