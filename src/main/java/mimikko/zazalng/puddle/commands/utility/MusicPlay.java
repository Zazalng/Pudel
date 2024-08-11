package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.manager.MusicManager;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicPlay implements Command {
    private final MusicManager musicManager = new MusicManager();

    @Override
    public void execute(MessageReceivedEvent e, String[] args) {
        if (args.length == 0) {
            e.getChannel().sendMessage("Please provide a YouTube URL.").queue();
            return;
        }

        String url = args[0];
        boolean isVoiceChannel = false;
        isVoiceChannel = e.getMember().getVoiceState().inAudioChannel();

        if (isVoiceChannel) {
            musicManager.loadAndPlay(e.getGuild(), url, e.getMember().getVoiceState().getChannel().asVoiceChannel());
            e.getChannel().sendMessage("Playing music from: " + url).queue();
        } else {
            e.getChannel().sendMessage("You must be in a voice channel to play music!").queue();
        }
    }
}
