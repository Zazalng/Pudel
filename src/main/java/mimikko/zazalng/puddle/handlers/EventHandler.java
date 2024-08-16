package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.commands.CommandManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter{
    protected final CommandManager commandManager;

    public EventHandler() {
        this.commandManager = new CommandManager();
    }

    public void

    public void onMessageReceived(MessageReceivedEvent e){
        e.getMessage().getMentions().getUsers().
        if(!e.getAuthor().isBot()){
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: "+e.getMessage().getContentRaw();
            System.out.println(fullRespond);
            e.getChannel().sendTyping().queue();

            String message = e.getMessage().getContentRaw();
            if (!message.startsWith(".")) return; // Assuming commands start with "."

            String[] parts = message.split(" ", 2); // Split into command and the rest of the message
            String command = parts[0].substring(1); // Extract the command without prefix
            String args = parts.length > 1 ? parts[1] : ""; // Handle arguments as a single string

            commandManager.handleCommand(command, e, args);
        }
        if(e.)
    }
}
