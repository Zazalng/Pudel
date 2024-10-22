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

    // Create a reusable template for an embed
    public EmbedBuilder createEmbed(SessionEntity session) {
        return new EmbedBuilder().setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                .setColor(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getColor())
                .setAuthor(session.getUser().getUserManager().getUserName(session), null, session.getUser().getJDA().getAvatarUrl())
                .setFooter(session.getCommand().getClass().getSimpleName() + " | " + session.getState(),session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder createHelper(SessionEntity session){
        return new EmbedBuilder().setTimestamp(ZonedDateTime.now(ZoneId.systemDefault()))
                .setColor(getPudelWorld().getPudelManager().getPudelEntity().)
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