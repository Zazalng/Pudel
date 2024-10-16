package mimikko.zazalng.pudel.commands.gacha;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class GachaDrawing extends AbstractCommand {

    @Override
    public GachaDrawing execute(SessionEntity session, String args) {
        super.execute(session, args);
        return this;
    }
    /**
     * @param session
     * @param args
     * @return
     */
    @Override
    public GachaDrawing initialState(SessionEntity session, String args) {
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle("Standard Gacha")
                        .setThumbnail("https://puu.sh/KgSjp.gif")
                        .addField("[SR] Janu","",true)
                        .addField("[SR] Janu","",true)
                        .addBlankField(false)
                        .addField("[SR] Janu","",true)
                        .addField("[SR] Janu","",true)
                        .addBlankField(false)
                        .addField("[SR] Janu","",true)
                        .addField("[SR] Janu","",true)
                        .addBlankField(false)
                        .addField("[SR] Janu","",true)
                        .addField("[SR] Janu","",true)
                        .addBlankField(false)
                        .addField("[SR] Janu","",true)
                        .addField("[SR] Janu","",true)
                        .build())
                .queue();
        return this;
    }

    /**
     * @return
     */
    @Override
    public GachaDrawing reload() {
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
