package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;

public abstract class EventHandler extends EventListener {
    protected final PuddleWorld puddleWorld;
    
    public EventHandler(PuddleWorld puddleWorld){
        this.puddleWorld = puddleWorld;
    }
}
