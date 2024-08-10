package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.PuddleWorld;
import mimikko.zazalng.puddle.commands.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter{
    protected final PuddleWorld puddleWorld;
    protected final CommandManager commandManager;

    public EventHandler(PuddleWorld puddleWorld) {
        this.puddleWorld = puddleWorld;
        this.commandManager = new CommandManager();
    }
    public void onMessageReceived(MessageReceivedEvent e){
        if(!e.getAuthor().isBot()){
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: "+e.getMessage().getContentRaw();
            System.out.println(fullRespond);
            e.getChannel().sendTyping().queue();

            String message = e.getMessage().getContentRaw();
            String[] parts = message.split(" ");
            String command = parts[0].substring(1); // Assumes commands start with a prefix like "!"
            String[] args = java.util.Arrays.copyOfRange(parts, 1, parts.length);

            commandManager.handleCommand(command, e, args);
        }
    }
}
