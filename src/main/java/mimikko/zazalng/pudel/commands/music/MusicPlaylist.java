package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaylist extends AbstractCommand{
    @Override
    public enum state {
        ADD,
        REMOVE,
        SWAP;
    }

    @Override
    public MusicPlaylist execute(SessionEntity session, String args) {
        switch (session.getState()) {
            case ADD:
                // Add logic
                break;
            case REMOVE:
                // Remove logic
                break;
            case SWAP:
                // Swap logic
                break;
            default:
                initialState(session, args);
                break;
        }
        return this;
    }

    @Override
    public MusicPlaylist initialState(SessionEntity session, String args) {
        session.setState(state.ADD); // Example initial state
        return this;
    }

    @Override
    public MusicPlaylist reload() {
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        return "";
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return "";
    }
}
