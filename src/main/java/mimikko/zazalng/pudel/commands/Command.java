package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command{
    <T extends Command> T execute(SessionEntity session, String args);
    <T extends Command> T execute(InteractionEntity interaction);
    <T extends Command> T reload(SessionEntity session);
    <T extends Command> T terminate(SessionEntity session);
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}