package mimikko.zazalng.pudel.logging;

import mimikko.zazalng.pudel.PudelWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldLogging {
    private final String className = "WorldLogging";
    protected final PudelWorld pudelWorld;
    private static final Logger logger = LoggerFactory.getLogger(WorldLogging.class);

    public WorldLogging(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
    }

    // Log at different levels
    public void info(String className, String methodName, String message) {
        logger.info(formatMessage(className, methodName, message));
    }

    public void debug(String className, String methodName, String message) {
        logger.debug(formatMessage(className, methodName, message));
    }

    public void error(String className, String methodName, String message, Throwable t) {
        logger.error(formatMessage(className, methodName, message), t);
    }

    private String formatMessage(String className, String methodName, String message) {
        // Customize the format if needed, e.g., prefix with the world name or environment info
        return String.format("[%s - %s] %s", className, methodName, message);
    }
}
