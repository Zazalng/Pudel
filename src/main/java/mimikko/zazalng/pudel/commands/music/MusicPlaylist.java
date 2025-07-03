package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

public class MusicPlaylist extends AbstractCommand{
    private state state;
    private enum state {
        REMOVE,
        SWAP
    }

    @Override
    public void execute(TextEntity session, String args) {
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
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(ReactionEntity interaction) {
    }

    @Override
    public String getDescription(TextEntity session) {
        return "";
    }

    @Override
    public String getDetailedHelp(TextEntity session) {
        return "";
    }

    public MusicPlaylist initialState(TextEntity session, String args) {
        session.getManager().getEmbedManager().embedCommand(session);
        return this;
    }

    private state getState(){
        return this.state;
    }

    private MusicPlaylist setState(state state){
        this.state = state;
        return this;
    }
}
