package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command{
    void execute(SessionEntity session, String args);
    void execute(InteractionEntity interaction);
    void reload();
    void terminate(SessionEntity session);
    void terminate(InteractionEntity interaction);
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}