package mimikko.zazalng.puddle.handlers;

import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class EventHandler extends ListenerAdapter{
    protected final Map<String, GuildEntity> guildEntity;
    protected final Map<String, UserEntity> userEntity;

    public EventHandler() {
        this.guildEntity = new HashMap<>();
        this.userEntity = new HashMap<>();
    }

    public GuildEntity getGuildEntity(Guild JDAguild){
        return this.guildEntity.computeIfAbsent(JDAguild.getId(), Entity -> new GuildEntity(JDAguild));
    }

    public UserEntity getUserEntity(User JDAuser){
        return this.userEntity.computeIfAbsent(JDAuser.getId(), Entity -> new UserEntity(JDAuser));
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        if(!e.getAuthor().isBot()){
            //From {guildName} in {channelName} by {userName} said: {contentRaw}
            String fullRespond = "From "+e.getGuild().getName()+" in "+e.getGuildChannel().getName()+" by "+e.getAuthor().getName()+" said: \n"+e.getMessage().getContentRaw();
            System.out.println(fullRespond);

            e.getChannel().sendTyping().queue();
            new CommandHandler(getGuildEntity(e.getGuild()), getUserEntity(e.getAuthor()), e);
        }
    }
}