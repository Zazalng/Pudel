package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;

@Deprecated
public class MusicLoop extends AbstractCommand{
    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(ReactionEntity interaction) {
        interaction.getManager().getMusicManager().getMusicPlayer(interaction).toggleLoop();
    }
}