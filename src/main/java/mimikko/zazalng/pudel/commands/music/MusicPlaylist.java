package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.contracts.Command.BaseCommandState;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaylist extends AbstractCommand<MusicPlaylist.state>{
    public enum state implements BaseCommandState {
        add,remove,swap;

        @Override
        public String getName() {
            return name();
        }
    }
    @Override
    public MusicPlaylist initialState(SessionEntity session, String args) {
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
