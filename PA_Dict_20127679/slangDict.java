//package PA_Dict_20127679;

import java.util.*;
import java.io.*;

public class slangDict {
    private static String fi = "slang.txt";
    private static String fo = "slang2.txt";

    private static Scanner input = new Scanner(System.in);
    private static Random rand = new Random();
    
    private static HashMap<String,ArrayList<ArrayList<String>>> dictionary = new HashMap<>();
    private static HashMap<String,ArrayList<ArrayList<String>>> history = new HashMap<>();

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
            String splitSlang = null;
            while (line!=null){
                String[] splitLine = line.split("`");
                    //update dictionary here 
                String[] splitDefinition = null;
                ArrayList<String> definition = null;
                if (splitLine.length>1){
                    splitDefinition = splitLine[1].split("\\| ");
                    definition = new ArrayList<>(Arrays.asList(splitDefinition));

                    if(dictionary.containsKey(splitLine[0])){
                        dictionary.get(splitLine[0]).add(definition);
                    }
                    else{
                        ArrayList<ArrayList<String>> def = new ArrayList<>();
                        def.add(definition);
                        dictionary.put(splitLine[0], def); 
                    }
                    splitSlang=splitLine[0];
                }
                else if(splitLine.length==1){
                    if (splitSlang!=null){
                        ArrayList<ArrayList<String>> def = dictionary.get(splitSlang);
                        def.add(new ArrayList<>(Arrays.asList(splitLine)));
                        dictionary.put(splitSlang, def); 
                    }
                }
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
            for(ArrayList<String> a:sortedDict.get(word)){
                s+=word+"`";
                if (a!=null){
                    for (String d : a){
                        s+=d+"| ";
                    }
                }
                s+="\n";
            }
        }
        writer.write(s);
        writer.close();
        System.out.println("Output Successfully!\n");
    }

        //need to save history?

    //search by slang word
    public boolean foundWord(String word){
        return dictionary.containsKey(word);
    }

    public ArrayList<ArrayList<String>> searchWord(String word){
        if (foundWord(word)){
            System.out.println("    Definition [OwO ]   :");
            String s="";
            ArrayList<ArrayList<String>> aa = dictionary.get(word);
            for (ArrayList<String> a:aa){
                for (String d : a){
                    s+=d+"\n";
                }
                s+="\n";
            }
            System.out.println(s);

            history.put(word, aa);
            return aa;
        }
        System.out.println("This slang is not in this dictionary (UmU)...\n");
        return null;
    }

    //search by definition
    public Set<String> searchDefinition(String definition){
        boolean check = false;
        HashMap<String,ArrayList<ArrayList<String>>> result = new HashMap<>();

        for (Map.Entry<String,ArrayList<ArrayList<String>>> e:dictionary.entrySet()){
            if (e.getValue()!=null){
                for (ArrayList<String> a : e.getValue()){
                    for (String d : a){
                        if (d.contains(definition)){
                        check = true;
                        result.put(e.getKey(), e.getValue());
                        }   
                    }
                }
            }
        }
        if(check){
            System.out.println("    Slang Result [OwO ]   :");
            for (Map.Entry<String,ArrayList<ArrayList<String>>> a:result.entrySet()){
                System.out.println(a.getKey()+"\n");
                history.put(a.getKey(), a.getValue());
            }
            return result.keySet();
        }
        else{
            System.out.println("We can not find the definition in this dictionary (UmU)...\n");
            return null;
        }
    }

    //show search history 
    public HashMap<String,ArrayList<ArrayList<String>>> viewSearchHistory(){
        System.out.println("    Search History [OwO ]   :");
        if (history.keySet().size() == 0){
            System.out.println("You haven't searched any slangs (ehe)>[UwU ]\n");
            return null;
        }
        for (String word:history.keySet()){
            ArrayList<ArrayList<String>> aa = history.get(word);
            for(ArrayList<String>a:aa){
                String s=word+"`";
                if (a!=null){
                    for (String d : a){
                        s+=d+"| ";
                    }
                }
                s+="\n";
                System.out.println(s);
            }
        }
        System.out.println();
        return history;
    }

    //add slang --> check condition: overwrite/duplicate
    public void addSlang(String word) throws IOException{
        if(foundWord(word)){ //exist
            System.out.println("The slang word already exists!! {-_- }");
            System.out.println("Enter 1 to overwrite; Enter 2 to add to definition; Enter 3 to duplicate this slang: ");
            int opt = input.nextInt();
            switch(opt){
                case 1:
                {
                    int idx=0;
                    if (dictionary.get(word).size()>1){
                        System.out.println("\nSearching for duplicated slang...  {~_~ }");
                        searchWord(word);                      
                        System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                        idx=input.nextInt()-1;
                        if(idx>=dictionary.get(word).size()){
                            System.out.print("Invalid option!! {-_- }");
                            return;
                        }
                    }
                    System.out.print("Enter new definition: ");
                    String nw = input.nextLine();
                    nw = input.nextLine();
                    String[] nl = new String[]{nw};
                    ArrayList<ArrayList<String>> d = dictionary.get(word);
                    d.get(idx).clear();
                    d.get(idx).addAll(new ArrayList<String>(Arrays.asList(nl)));
                    //dictionary.put(word,d);
                    System.out.println("Updated!! {~_~ } \n");
                    outputDict(fo);
                    return;
                }
                case 2: 
                {
                    int idx=0;
                    if (dictionary.get(word).size()>1){
                        System.out.println("\nSearching for duplicated slang...  {~_~ }");
                        searchWord(word);                      
                        System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                        idx=input.nextInt()-1;
                        if(idx>=dictionary.get(word).size()){
                            System.out.print("Invalid option!! {-_- }");
                            return;
                        }
                    }

                    System.out.print("Enter new definition: ");
                    String nw = input.nextLine();
                    nw = input.nextLine();
                    dictionary.get(word).get(idx).add(nw);
                    //dictionary.put(word,dictionary.get(word));
                    System.out.println("Updated!! {~_~ } \n");
                    outputDict(fo);
                    return;
                }
                case 3:
                {
                    int idx=0;
                    if (dictionary.get(word).size()>1){
                        System.out.println("\nSearching for duplicated slang...  {~_~ }");
                        searchWord(word);                      
                        System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                        idx=input.nextInt()-1;
                        if(idx>=dictionary.get(word).size()){
                            System.out.print("Invalid option!! {-_- }");
                            return;
                        }
                    }

                    //deep copy
                    ArrayList<String> na = new ArrayList<>();
                    for (String a:dictionary.get(word).get(idx)){
                        na.add(a);
                    }
                    dictionary.get(word).add(na);

                    System.out.println("Duplicated!! {~_~ } \n");
                    outputDict(fo);
                    return;
                }
                default:
                {
                    System.out.print("Invalid number!! {-_- }");
                    return;
                }
            }
        }
        System.out.print("Enter new definition: ");
        String nw = input.nextLine();
        nw = input.nextLine();
        String[] nl = new String[]{nw};
        ArrayList<ArrayList<String>> d = new ArrayList<>();
        d.add(new ArrayList<String>(Arrays.asList(nl)));
        dictionary.put(word,d);
        System.out.println("Updated!! {~_~ } \n");
        outputDict(fo);
    }

        //swing ver

    public boolean checkDup(String word){
        return (dictionary.get(word).size()>1);
    }

    public ArrayList<ArrayList<String>> getDefinition(String word){
        return dictionary.get(word);
    }
    
    public boolean overwrite(String word, String definition, int idx) throws IOException{
        String[] nl = new String[]{definition};
        ArrayList<ArrayList<String>> d = dictionary.get(word);
        d.get(idx).clear();
        d.get(idx).addAll(new ArrayList<String>(Arrays.asList(nl)));
        System.out.println("Updated!! {~_~ } \n");
        outputDict(fo);
        return true;
    }

    public boolean append(String word, String definition, int idx) throws IOException{
        dictionary.get(word).get(idx).add(definition);
        System.out.println("Updated!! {~_~ } \n");
        outputDict(fo);
        return true;    
    }
    
    public void duplicate(String word, int idx) throws IOException{
        //deep copy
        ArrayList<String> na = new ArrayList<>();
        for (String a:dictionary.get(word).get(idx)){
            na.add(a);
        }
        dictionary.get(word).add(na);
        
        System.out.println("Duplicated!! {~_~ } \n");
        outputDict(fo);
        return;
    }
    
    public void add(String word,String definition) throws IOException{
        String[] nl = new String[]{definition};
        ArrayList<ArrayList<String>> d = new ArrayList<>();
        d.add(new ArrayList<String>(Arrays.asList(nl)));
        dictionary.put(word,d);
        outputDict(fo);
    }
    
    //edit slang
    public void editSlang(String word) throws IOException{
        if(foundWord(word)){
            while(true){
                System.out.println("What do you want to edit? Enter 1 to replace word; Enter 2 to replace definition; Enter 3 to add to definition: ");   
                int opt = input.nextInt();
                switch(opt){
                    case 1:
                    {
                        int idx=0;
                        if (dictionary.get(word).size()>1){  
                            System.out.println("\nSearching for duplicated slang...  {~_~ }");
                            searchWord(word);                      
                            System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                            idx=input.nextInt()-1;
                            if(dictionary.get(word).size()<=idx){
                                System.out.print("Invalid option!! {-_- } ");
                                return;
                            }
                        }

                        System.out.print("Enter new slang word: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();
                        if(foundWord(nw)){
                            System.out.println("The slang word already exists!! Please go to add slang... {-_- } \n");
                            return;
                        }
                        else{
                            if(dictionary.get(word).size()>1){
                                ArrayList<ArrayList<String>> d = new ArrayList<>();
                                //deep copy
                                ArrayList<String> na = new ArrayList<>();
                                for (String a:dictionary.get(word).get(idx)){
                                    na.add(a);
                                }

                                d.add(na);
                                dictionary.get(word).remove(idx);
                                dictionary.put(nw,d);
                                System.out.println("Updated!! {~_~ } \n");
                                outputDict(fo);
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
                    }
                    case 2:
                    {
                        int idx=0;
                        if (dictionary.get(word).size()>1){
                            System.out.println("\nSearching for duplicated slang...  {~_~ }");
                            searchWord(word);                        
                            System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                            idx=input.nextInt()-1;
                            if(dictionary.get(word).size()<=idx){
                                System.out.print("Invalid option!! {-_- } ");
                                return;
                            }
                        }
                        
                        int n =0;
                        if(dictionary.get(word).get(idx).size()>1){
                            System.out.println("There are "+String.valueOf(dictionary.get(word).get(idx).size())+" definitions. Enter number of the definition to replace: ");
                            n = input.nextInt()-1;
                                
                            if(dictionary.get(word).get(idx).size()<=n){
                                System.out.print("Invalid option!! {-_- } ");
                                return;
                            }
                        }

                        System.out.print("Enter new definition: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();

                        dictionary.get(word).get(idx).set(n, nw);

                        //dictionary.put(word,d);
                        System.out.println("Updated!! {~_~ } \n");
                        outputDict(fo);
                        return;
                    }
                    case 3:
                    {
                        int idx=0;
                        if (dictionary.get(word).size()>1){
                            System.out.println("\nSearching for duplicated slang...  {~_~ }");
                            searchWord(word);                        
                            System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                            idx=input.nextInt()-1;
                            if(dictionary.get(word).size()<=idx){
                                System.out.print("Invalid option!! {-_- } ");
                                return;
                            }
                        }

                        System.out.print("Enter new definition: ");
                        String nw = input.nextLine();
                        nw = input.nextLine();
                        dictionary.get(word).get(idx).add(nw);
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

    // swing ver
    public boolean checkMult(String word, int idx){
       return dictionary.get(word).get(idx).size()>1;
    }

    public void editWord(String word, String nw, int idx) throws IOException{
        if(dictionary.get(word).size()>1){
            ArrayList<ArrayList<String>> d = new ArrayList<>();
            //deep copy
            ArrayList<String> na = new ArrayList<>();
            for (String a:dictionary.get(word).get(idx)){
                na.add(a);
            }

            d.add(na);
            dictionary.get(word).remove(idx);
            dictionary.put(nw,d);
            System.out.println("Updated!! {~_~ } \n");
            outputDict(fo);
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

    public void editDefinition(String word, String nw, int idx, int n) throws IOException{
        dictionary.get(word).get(idx).set(n, nw);
        System.out.println("Updated!! {~_~ } \n");
        outputDict(fo);
        return;
    }

    //delete slang --> need confirm
    public void deleteSlang(String word) throws IOException{
        if(foundWord(word)){
            int idx=0;
            if(dictionary.get(word).size()>1){
                System.out.println("\nSearching for duplicated slang...  {~_~ }");
                searchWord(word);
                System.out.print("The entered slang is duplicated. Enter number from 1 to "+String.valueOf(dictionary.get(word).size())+" to select: ");
                idx=input.nextInt()-1;
                if(dictionary.get(word).size()<=idx){
                    System.out.print("Invalid option!! {-_- } ");
                    return;
                }
            }
            
            System.out.println("Do you want to delete this slang? Enter 1 for Yes, other numbers for No: ");   
            int opt = input.nextInt();
            if (opt!=1){
                System.out.println("Returning to menu... {-_- }\n"); 
                return;
            } 

            if(dictionary.get(word).size()>1){
                dictionary.get(word).remove(idx);
            }
            else{
                dictionary.remove(word);
            }
            //sortedDict.remove(word);
            System.out.println("Slang is deleted! <OAO >\n"); 
            outputDict(fo);  
        }
        else{
            System.out.println("This slang is not in this dictionary (UmU)...\n");
        }
    }

    //swing ver
    public void delete(String word, int idx) throws IOException{
        if(dictionary.get(word).size()>1){
            dictionary.get(word).remove(idx);
        }
        else{
            dictionary.remove(word);
        }
        //sortedDict.remove(word);
        System.out.println("Slang is deleted! <OAO >\n"); 
        outputDict(fo);  
    }

    //reset slang list
    public void resetDict(){
        System.out.println("Do you want to reset the dictionary? Enter 1 for Yes, other numbers for No: ");   
        int opt = input.nextInt();
        if (opt!=1){
            return;
        } 
        inputDict(fi); 
        System.out.println("List is reset!! <0A0 >\n");   
        try{
            outputDict(fo);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //random slang (On this day slang word)
    public String randomS(){
        int len = dictionary.keySet().size();
        int idx = rand.nextInt(len) - 1;
    
        Object[] set = dictionary.keySet().toArray();

        return String.valueOf(set[idx]);
    }

    public ArrayList<String> randomD(String word){
        int len = dictionary.get(word).size();
        int idx = rand.nextInt(len) - 1;
    
        ArrayList<String> a = dictionary.get(word).get(idx);

        return a;
    }

    public void randomSlang(){
        String slang = randomS();
        ArrayList<String> a = randomD(slang);

        System.out.println("    On this day slang word (^o^)/   :");
        System.out.println(" Slang: "+String.valueOf(slang));
        System.out.println(" Definition: ");
        String s="";

        for (String d : a){
            s+=d+"\n";
        }
        
        System.out.println(s);
    }

    //minigame: 1 slang 4 definition options
    public void wordMinigame(){
        String slang = randomS();

        HashMap<String,ArrayList<ArrayList<String>>> al = new HashMap<>();
        al.put(slang,dictionary.get(slang));

        for(int i=0;i<3;i++){
            String s = randomS();
            al.put(s,dictionary.get(s));
        }

        //display quiz
        System.out.println("    (._. ) < Slang: "+slang);
        System.out.println("    ( ._.) < Guess the definition of the slang! Enter number from 1-4:");
        int count=0;
        for(ArrayList<ArrayList<String>> aa:al.values()){
            for(ArrayList<String> a:aa){
                count+=1;
                String str="";
                for(String s:a){
                    str+=s + "| ";
                }
                System.out.println(String.valueOf(count)+") "+str);
            }
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
        ArrayList<ArrayList<String>> definition = dictionary.get(slang);
        ArrayList<String> al = new ArrayList<>();
        al.add(slang);

        for(int i=0;i<3;i++){
            al.add(randomS());
        }

        Collections.shuffle(al);

        //display quiz
        System.out.println("    (._. ) < Definition: ");
        for(ArrayList<String> a:definition){
            for(String s:a){
                System.out.println(s);
            }
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
    LinkedHashMap<String,ArrayList<ArrayList<String>>> sortedDict = new LinkedHashMap<>();
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
            for (ArrayList<String> a:sortedDict.get(word)){
                String s=word+"`";
                if (a!=null){
                    for (String d : a){
                        s+=d+"| ";
                    }
                }
                System.out.println(s);
            }
        }
        System.out.println();
    }

}