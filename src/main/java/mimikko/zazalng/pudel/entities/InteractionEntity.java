package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.contracts.InteractionType;
import mimikko.zazalng.pudel.manager.SessionManager;

public interface InteractionEntity {
    InteractionEntity asInteractionEntity();
    InteractionType getType();
    SessionManager getManager();
    GuildEntity getGuild();
    UserEntity getUser();
    String getKey(PudelWorld pudelWorld);
    void execute(Object obj);
    void terminate();
}