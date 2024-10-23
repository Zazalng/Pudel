package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command{
    Command execute(SessionEntity session, String args);
    Command initialState(SessionEntity session, String args);
    Command reload();
    String getState();
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}