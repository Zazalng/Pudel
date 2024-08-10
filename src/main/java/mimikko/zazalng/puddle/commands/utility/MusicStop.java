package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.manager.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicStop implements Command {
    private final MusicManager musicManager = new MusicManager();

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        musicManager.stop(e.getGuild());
        e.getChannel().sendMessage("Stopping music...").queue();
    }
}
