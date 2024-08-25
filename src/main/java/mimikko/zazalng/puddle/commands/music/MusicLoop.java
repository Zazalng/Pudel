package mimikko.zazalng.puddle.commands.music;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

import static mimikko.zazalng.puddle.utility.BooleanUtility.*;

public class MusicLoop implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(triggerTrue(args)){
            guild.getMusicManager().setLoop(true);
        } else if(triggerFalse(args)){
            guild.getMusicManager().setLoop(false);
        } else{
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current setting for Looping is `"+guild.getMusicManager().isLoop()+"`").queue();
            return;
        }
        guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Looping to `"+guild.getMusicManager().isLoop()+"`").queue();
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