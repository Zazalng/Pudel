package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicLoop implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(triggerTrue(args)){
            guild.getMusicPlayer().setLoop(true);
        } else if(triggerFalse(args)){
            guild.getMusicPlayer().setLoop(false);
        } else{
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current setting for Looping is `"+guild.getMusicPlayer().isLoop()+"`").queue();
            return;
        }
        guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDA().getId()).getNickname()+" has setting for Looping to `"+guild.getMusicPlayer().isLoop()+"`").queue();
    }

    @Override
    public void execute(SessionEntity session, String args) {
        if(triggerTrue(args)){

        }
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Toggles the looping of the current track.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: loop [args]" +
                "\nExamples: `p!loop`, `p!loop 0`, `p!loop true`" +
                "\n\n`false` `0` `off` `disable` will result set loop as `off`." +
                "\n`true` `1` `on` `enable` will result set loop as `on`." +
                "\nNo argument will result as showing current setting loop.";
    }
}