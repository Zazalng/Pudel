package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import net.dv8tion.jda.api.EmbedBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmbedManager implements Manager {

    private final PudelWorld pudelWorld; // Your bot world
    private final Logger logger = LoggerFactory.getLogger(EmbedManager.class);

    public EmbedManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    // Create a reusable template for an embed
    public EmbedBuilder createEmbed() {
        return new EmbedBuilder();
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