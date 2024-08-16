package mimikko.zazalng.puddle.utility;

import mimikko.zazalng.puddle.PuddleWorld;

public class WorldLogging {
    private String mergeRespond;

    public WorldLogging(){
        this.mergeRespond = "";
    }

    public void logoutput(String mergeRespond){
        System.out.println(mergeRespond);
    }

    public String RespondBuilder(String longText){
        return this.mergeRespond;
    }
}
