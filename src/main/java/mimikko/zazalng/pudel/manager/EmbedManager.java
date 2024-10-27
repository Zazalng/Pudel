package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class EmbedManager implements Manager {

    private final PudelWorld pudelWorld; // Your bot world
    private final Logger logger = LoggerFactory.getLogger(EmbedManager.class);

    public EmbedManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    private EmbedBuilder createEmbed(SessionEntity session){
        return new EmbedBuilder().setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                .setAuthor(session.getUser().getUserManager().getUserName(session), null, session.getUser().getJDA().getAvatarUrl());
    }

    public EmbedBuilder embedCommand(SessionEntity session) {
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getColor())
                .setFooter(session.getCommand().getClass().getSimpleName(),session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder embedHelp(SessionEntity session){
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(getPudelWorld().getPudelManager().getPudelEntity().getJDA()).getColor())
                .setThumbnail("https://puu.sh/KgdPy.gif")
                .setTitle(getPudelWorld().getLocalizationManager().getLocalizedText(session, "command.available",null))
                .setFooter("Help Command",session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder embedDetail(SessionEntity session){
        return createEmbed(session)
                .setColor(session.getGuild().getJDA().getMember(getPudelWorld().getPudelManager().getPudelEntity().getJDA()).getColor())
                .setThumbnail("https://puu.sh/KgdPy.gif")
                .setFooter("Help Command",session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder castEmbedBuilder(SessionEntity session, String key){
        return (EmbedBuilder) session.getData(key,false);
    }

    @Override
    public PudelWorld getPudelWorld() {
        return pudelWorld;
    }

    @Override
    public EmbedManager initialize() {
        logger.info("EmbedManager initialized.");
        return this;
    }

    @Override
    public EmbedManager reload() {
        logger.info("EmbedManager reloaded.");
        return this;
    }

    @Override
    public EmbedManager shutdown() {
        logger.info("EmbedManager shut down.");
        return this;
    }
}