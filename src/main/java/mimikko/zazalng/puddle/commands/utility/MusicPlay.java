package mimikko.zazalng.puddle.commands.utility;

import mimikko.zazalng.puddle.commands.Command;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class MusicPlay implements Command {

    @Override
    public void execute(GuildEntity guild, UserEntity user, String replyChannel, String args) {
        if (args.isEmpty()) {
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Please provide a song title or a YouTube URL.").queue();
            return;
        }

        boolean isVoiceChannel = guild.getGuild().getMemberById(user.getJDAuser().getId()).getVoiceState().inAudioChannel();
        if (isVoiceChannel) {
            String input = String.join(" ", args);

            // Automatically prepend "ytsearch:" if the input isn't a URL
            if (!input.startsWith("http://") && !input.startsWith("https://")) {
                input = "ytsearch:" + input;
            }
            //musicManager.loadAndPlay(input, e.getMember().getVoiceState().getChannel().asVoiceChannel());
            guild.getMusicManager().loadAndPlay(input,guild.getGuild().getMemberById(user.getJDAuser().getId()).getVoiceState().getChannel().asVoiceChannel());
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Searching and playing: " + input).queue();
        } else {
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("You must be in a voice channel to play music!").queue();
        }
    }
}
