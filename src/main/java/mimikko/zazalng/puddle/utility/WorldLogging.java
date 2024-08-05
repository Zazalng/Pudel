package mimikko.zazalng.puddle.utility;

import mimikko.zazalng.puddle.PuddleWorld;

public class WorldLogging {
    protected final PuddleWorld puddleWorld;
    private String mergeRespond;

    public WorldLogging(PuddleWorld puddleWorld){
        this.puddleWorld = puddleWorld;
        this.mergeRespond = "";
    }

    public PuddleWorld getPuddleWorld() {
        return this.puddleWorld;
    }

    public void logoutput(String mergeRespond){
        System.out.println(mergeRespond);
    }

    public String RespondBuilder(String longText){
        return this.mergeRespond;
    }
}
