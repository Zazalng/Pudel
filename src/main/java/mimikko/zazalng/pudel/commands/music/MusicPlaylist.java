package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlaylist extends AbstractCommand{
    /**
     * @param session
     * @param args
     * @return
     */
    @Override
    public MusicPlaylist initialState(SessionEntity session, String args) {
        return this;
    }

    /**
     * @return
     */
    @Override
    public MusicPlaylist reload() {
        return this;
    }

    /**
     * @param session
     * @return
     */
    @Override
    public String getDescription(SessionEntity session) {
        return "";
    }

    /**
     * @param session
     * @return
     */
    @Override
    public String getDetailedHelp(SessionEntity session) {
        return "";
    }
}
