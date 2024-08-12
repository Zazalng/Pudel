package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.manager.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicPlay implements Command {
    private final MusicManager musicManager = new MusicManager();

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        if (args.length == 0) {
            e.getChannel().sendMessage("Please provide a song title or a YouTube URL.").queue();
            return;
        }

        boolean isVoiceChannel = e.getMember().getVoiceState().inAudioChannel();

        if (isVoiceChannel) {
            String input = String.join(" ", args);

            // Automatically prepend "ytsearch:" if the input isn't a URL
            if (!input.startsWith("http://") && !input.startsWith("https://")) {
                input = "ytsearch:" + input;
            }

            musicManager.loadAndPlay(e.getGuild(), input, e.getMember().getVoiceState().getChannel().asVoiceChannel());
            e.getChannel().sendMessage("Searching and playing: " + input).queue();
        } else {
            e.getChannel().sendMessage("You must be in a voice channel to play music!").queue();
        }
    }
}
