package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EmbedManager extends AbstractManager {
    private final Logger logger = LoggerFactory.getLogger(EmbedManager.class);

    protected EmbedManager(PudelWorld pudelWorld) {
        super(pudelWorld);
    }

    private EmbedBuilder createEmbed(TextEntity session){
        return new EmbedBuilder().setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                .setAuthor(session.getUser().getName(), null, session.getUser().getJDA().getAvatarUrl());
    }

    public EmbedBuilder embedCommand(TextEntity session) {
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getColor())
                .setFooter(session.getCommand().getClass().getSimpleName(),session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder embedHelp(TextEntity session){
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(getPudelManager().getPudelEntity().getJDA()).getColor())
                .setThumbnail("https://puu.sh/KgdPy.gif")
                .setTitle(getLocalizationManager().getLocalizedText(session, "command.available",null))
                .setFooter("Help Command",session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder embedDetail(TextEntity session){
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(getPudelManager().getPudelEntity().getJDA()).getColor())
                .setThumbnail("https://puu.sh/KgdPy.gif")
                .setFooter("Help Command",session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder castEmbedBuilder(TextEntity session, String key){
        return (EmbedBuilder) session.getData(key,false);
    }

    public EmbedBuilder castEmbedBuilder(MessageEmbed msg){
        return new EmbedBuilder(msg);
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}