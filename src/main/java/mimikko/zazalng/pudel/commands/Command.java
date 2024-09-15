package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command {
    void execute(SessionEntity session, String args);
    void reload();
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}