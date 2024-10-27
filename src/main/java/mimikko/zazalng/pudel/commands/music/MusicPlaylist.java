package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaylist extends AbstractCommand{
    private state state;
    private Object lookupTrack;
    private enum state {
        REMOVE,
        SWAP
    }

    @Override
    public MusicPlaylist execute(SessionEntity session, String args) {
        switch (getState()) {
            case REMOVE:
                // Remove logic
                break;
            case SWAP:
                // Swap logic
                break;
            case null:
                initialState(session, args);
                break;
        }
        return this;
    }

    public MusicPlaylist initialState(SessionEntity session, String args) {
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

    private state getState(){
        return this.state;
    }

    private MusicPlaylist setState(state state){
        this.state = state;
        return this;
    }
}
