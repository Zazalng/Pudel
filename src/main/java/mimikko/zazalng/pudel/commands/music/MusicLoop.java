package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import org.jetbrains.annotations.Nullable;

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

    @Override
    public void execute(InteractionEntity session, @Nullable String args) {

    }
}