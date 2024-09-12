package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class MusicPlay extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            args = "Please provide a song title or a YouTube URL.";
            session.getChannel().sendMessage(args).queue();
            session.setState("END");
            return;
        }

        if (!session.getUser().getUserManager().isVoiceActive(session.getGuild().getJDA(), session.getUser().getJDA())) {
            session.getChannel().sendMessage("You must be in a voice channel to play music!").queue();
            session.setState("END");
            return;
        }

        String finalArgs = args; // Make args effectively final for use in the lambda
        String trackUrl = args.startsWith("http://") || args.startsWith("https://") ? finalArgs : "ytsearch:" + finalArgs;

        session.getPudelWorld().getMusicManager().loadAndPlay(session.getGuild().getMusicPlayer(), trackUrl, result -> {
            // This block will execute once loadAndPlay finishes
            session.getChannel().sendMessage(result).queue();

            if (!result.contains("No Match") && !result.contains("Load failed")) {
                session.getGuild().getJDA().getAudioManager().setSendingHandler(session.getGuild().getMusicPlayer().getPlayer());
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(
                        session.getGuild().getJDA(),
                        session.getGuild().getAsMember(session.getUser().getJDA()).getVoiceState().getChannel().asVoiceChannel()
                );
            }

            session.setState("END");
        });
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
