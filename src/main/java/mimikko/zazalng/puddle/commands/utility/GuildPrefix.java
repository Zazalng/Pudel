package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildPrefix implements Command {
    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        if(args.length == 0){
            e.getMessage().getChannel().sendMessage("Prefix for this server / guild is `");
        } else{

        }
    }
}
