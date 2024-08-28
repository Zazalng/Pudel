package mimikko.zazalng.pudel.logging;

import mimikko.zazalng.pudel.PudelWorld;

public class WorldLogging {
    protected PudelWorld pudleWorld;
    private String mergeRespond;

    public WorldLogging(PudelWorld pudelWorld){
        this.mergeRespond = "";
    }

    public void logoutput(String mergeRespond){
        System.out.println(mergeRespond);
    }

    public String RespondBuilder(String longText){
        return this.mergeRespond;
    }
}
