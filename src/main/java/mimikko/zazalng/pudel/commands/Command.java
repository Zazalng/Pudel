package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.contracts.SessionState;
import mimikko.zazalng.pudel.entities.SessionEntity;

public interface Command<S extends Enum<S> & SessionState> {
    <T extends Command<S>> T execute(SessionEntity session, String args);
    <T extends Command<S>> T initialState(SessionEntity session, String args);
    <T extends Command<S>> T reload();
    String getDescription(SessionEntity session);
    String getDetailedHelp(SessionEntity session);
}
