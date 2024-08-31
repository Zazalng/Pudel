package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicShuffle implements Command {
    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if(triggerTrue(args)){
            guild.getMusicManager().setShuffle(true);
        } else if(triggerFalse(args)){
            guild.getMusicManager().setShuffle(false);
        } else if(args.isEmpty()){
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current setting for Shuffle player is `"+guild.getMusicManager().isShuffle()+"`").queue();
            return;
        } else{
            guild.getMusicManager().shufflePlaylist();
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("The current playlist has been shuffle by seed `"+args+"`").queue();
            return;
        }
        guild.getGuild().getTextChannelById(replyChannel).sendMessage(guild.getGuild().getMemberById(user.getJDAuser().getId()).getNickname()+" has setting for Shuffle player to `"+guild.getMusicManager().isShuffle()+"`").queue();
    }

    @Override
    public String getDescription() {
        return "Toggles the shuffle of the playlist or scrambled playlist 1 time.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: shuffle [args]" +
                "\nExamples: `p!shuffle`, `p!shuffle 0`, `p!shuffle true` `p!shuffle 85d6w4d65asdcdw`" +
                "\n\n`false` `0` `off` `disable` will result set loop as `off`." +
                "\n`true` `1` `on` `enable` will result set loop as `on`." +
                "\nother string will scrambled playlist by String seed." +
                "\nNo argument will result as showing current setting loop.";
    }
}
