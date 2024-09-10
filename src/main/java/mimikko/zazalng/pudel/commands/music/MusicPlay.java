package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class MusicPlay extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            args = "Please provide a song title or a YouTube URL.";
        } else {
            if (session.getUser().getUserManager().isVoiceActive(session.getGuild().getJDA(),session.getUser().getJDA())) {
                // Automatically prepend "ytsearch:" if the input isn't a URL
                if (!args.startsWith("http://") && !args.startsWith("https://")) {
                    session.getPudelWorld().getMusicManager().loadAndPlay(session.getGuild().getMusicPlayer(),"ytsearch:" + args);
                } else {
                    //guild.getMusicPlayer().loadAndPlay(input, guild.getGuild().getMemberById(user.getJDA().getId()).getVoiceState().getChannel().asVoiceChannel());
                }
                //guild.getGuild().getTextChannelById(replyChannel).sendMessage("Searching and playing: \n`" + input + "`").queue();
            } else {
                //guild.getGuild().getTextChannelById(replyChannel).sendMessage("You must be in a voice channel to play music!").queue();
            }
        }
    }

    @Override
    public void reload() {

    }

    @Override
    public String getDescription() {
        return "Perform queuing into playlist or loaded entire playlist from given url.";
    }

    @Override
    public String getDetailedHelp() {
        return "Usage: play {args}" +
                "\nExample: `p!play bubble - veela` / `p!play https://www.youtube.com/watch?v=rDVE6bXdNQ4`" +
                "\n\nIf {args} was not URL, it will perform searching {args} into youtube and add top search to queuing." +
                "\nIf {args} was URL playlist, it will perform queuing all video from that URL playlist." +
                "\nIf {args} was URL video, it will perform queuing that video.";
    }
}
