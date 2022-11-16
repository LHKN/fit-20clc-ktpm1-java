package PA_Dict_20127679;

public class slangCard {
    private String word;
    private String[] definition;
    
    public slangCard(String word, String[] definition){
        setWord(word);
        setDefinition(definition);
    }
    public String getWord(){
        return word;
    }
    public String[] getDefinition(){
        return definition;
    }

    public void setWord(String word){
        this.word=word;
    }
    public void setDefinition(String[] definition){
        this.definition=definition;
    }
}
