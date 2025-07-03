package mimikko.zazalng.pudel.commands.dev;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;

import static mimikko.zazalng.pudel.utility.StringUtility.stringFormat;

public class DevEmojiUnicode extends AbstractCommand {
    /**
     * @param session
     * @param args
     * @return
     */
    @Override
    public void execute(TextEntity session, String args) {
        initialState(session);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(ReactionEntity interaction) {
        initialState(interaction);
    }

    private DevEmojiUnicode initialState(TextEntity session){
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle("Debug get Emoji Unicode name")
                .setThumbnail("https://puu.sh/KgAxn.gif")
                .setDescription("`Placeholder`")
                .build()
        ).queue(e -> session.getManager().getReactionManager().newReaction(e, session, 10));
        super.terminate(session);
        return this;
    }

    private DevEmojiUnicode initialState(ReactionEntity interaction) {
        System.out.println(interaction.getReact());
        interaction.getMessage().editMessageEmbeds(interaction.getManager().getEmbedManager().castEmbedBuilder(interaction.getMessage().getEmbeds().getFirst())
                .setDescription(stringFormat("`%s`",interaction.getReact()))
                .build()).queue();
        return this;
    }
}
