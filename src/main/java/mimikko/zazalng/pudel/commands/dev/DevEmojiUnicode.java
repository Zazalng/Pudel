package mimikko.zazalng.pudel.commands.dev;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.StringUtility.stringFormat;

public class DevEmojiUnicode extends AbstractCommand {
    /**
     * @param session
     * @param args
     * @return
     */
    @Override
    public void execute(SessionEntity session, String args) {
        initialState(session);
    }

    /**
     * @param interaction
     * @return
     */
    @Override
    public void execute(InteractionEntity interaction) {
        initialState(interaction);
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

    private DevEmojiUnicode initialState(SessionEntity session){
        session.getChannel().sendMessageEmbeds(session.getManager().getEmbedManager().embedCommand(session)
                .setTitle("Debug get Emoji Unicode name")
                .setThumbnail("https://puu.sh/KgAxn.gif")
                .setDescription("`Placeholder`")
                .build()
        ).queue(e -> session.getManager().getInteractionManager().newInteraction(e, session, 10));
        super.terminate(session);
        return this;
    }

    private DevEmojiUnicode initialState(InteractionEntity interaction) {
        System.out.println(interaction.getReact());
        interaction.getMessage().editMessageEmbeds(interaction.getManager().getEmbedManager().castEmbedBuilder(interaction.getMessage().getEmbeds().getFirst())
                .setDescription(stringFormat("`%s`",interaction.getReact()))
                .build()).queue();
        return this;
    }
}
