package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class EmbedManager implements Manager {

    private final PudelWorld pudelWorld; // Your bot world
    private final Logger logger = LoggerFactory.getLogger(EmbedManager.class);

    public EmbedManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    // Create a reusable template for an embed
    public EmbedBuilder createBasicEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.BLUE);  // Set a default color
        builder.setFooter("Powered by PudelBot");  // Set a default footer
        return builder;
    }

    // Example method to create a help embed
    public EmbedBuilder createHelpEmbed(String title, String description) {
        EmbedBuilder builder = createBasicEmbed();
        builder.setTitle(title);
        builder.setDescription(description);
        return builder;
    }

    // Example method to create an error embed
    public EmbedBuilder createErrorEmbed(String errorMessage) {
        EmbedBuilder builder = createBasicEmbed();
        builder.setColor(Color.RED);  // Change color for errors
        builder.setTitle("Error");
        builder.setDescription(errorMessage);
        return builder;
    }

    // Process a finished embed
    public void sendEmbed(SessionEntity session, EmbedBuilder embedBuilder) {
        session.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
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