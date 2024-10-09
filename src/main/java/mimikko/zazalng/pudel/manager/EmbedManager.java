package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
                .setAuthor(session.getUser().getUserManager().getUserName(session),null,session.getUser().getJDA().getAvatarUrl())
                .setFooter(session.getCommand().getClass().getSimpleName() + " | " + session.getState(),session.getGuild().getJDA().getIconUrl());
    }

    public EmbedBuilder castEmbedBuilder(SessionEntity session, String key){
        return (EmbedBuilder) session.getData(key,false);
    }

    public EmbedManager sendingEmbed(SessionEntity session, MessageEmbed embed){
        session.getChannel().sendMessageEmbeds(embed).queue();
        return this;
    }

    @Override
    public PudelWorld getPudelWorld() {
        return pudelWorld;
    }

    @Override
    public void initialize() {
        logger.info("EmbedManager initialized.");
    }

    @Override
    public void reload() {
        logger.info("EmbedManager reloaded.");
    }

    @Override
    public void shutdown() {
        logger.info("EmbedManager shut down.");
    }
}