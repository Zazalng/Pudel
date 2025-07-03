package mimikko.zazalng.pudel.manager.session;

import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import net.dv8tion.jda.api.events.Event;

public interface SessionInterface<T extends InteractionEntity, E extends Event> {
    InteractionType getType();
    T createSession(E event); // handle + register internally
    void terminateSession(T session); // optionally cleanup logic
}