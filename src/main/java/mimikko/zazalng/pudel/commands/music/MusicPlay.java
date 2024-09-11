package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicPlay extends AbstractCommand {
    private static final Logger logger = LoggerFactory.getLogger(MusicPlay.class);
    @Override
    public void execute(SessionEntity session, String args) {
        logger.debug("State: " + session.getState() + " | args: "+args);
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            args = "Please provide a song title or a YouTube URL.";
        } else {
            if (session.getUser().getUserManager().isVoiceActive(session.getGuild().getJDA(),session.getUser().getJDA())) {
                logger.debug("State: " + session.getState() + " | args: "+args);
                // Automatically prepend "ytsearch:" if the input isn't a URL
                if (!args.startsWith("http://") && !args.startsWith("https://")) {
                    logger.debug("Beforeload | State: " + session.getState() + " | args: "+args);
                    args = session.getPudelWorld().getMusicManager().loadAndPlay(session.getGuild().getMusicPlayer(),"ytsearch:" + args);
                    logger.debug("Afterload | State: " + session.getState() + " | args: "+args);
                } else {
                    args = session.getPudelWorld().getMusicManager().loadAndPlay(session.getGuild().getMusicPlayer(),args);
                }
                session.getGuild().getJDA().getAudioManager().setSendingHandler(session.getGuild().getMusicPlayer().getPlayer());
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session.getGuild().getJDA(),session.getGuild().getAsMember(session.getUser().getJDA()).getVoiceState().getChannel().asVoiceChannel());
            } else {
                args = "You must be in a voice channel to play music!";
            }
        }
        session.getChannel().sendMessage(args).queue();

        session.setState("END");
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
