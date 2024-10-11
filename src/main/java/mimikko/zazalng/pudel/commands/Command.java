package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command {
    <T extends Command> T execute(SessionEntity session, String args);
    <T extends Command> T initialState(SessionEntity session, String args);
    <T extends Command> T reload();
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}