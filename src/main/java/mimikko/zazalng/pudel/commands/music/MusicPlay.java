package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

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
                guild.getMusicPlayer().loadAndPlay("ytsearch:" + input,guild.getGuild().getMemberById(user.getJDAuser().getId()).getVoiceState().getChannel().asVoiceChannel());
            } else{
                guild.getMusicPlayer().loadAndPlay(input,guild.getGuild().getMemberById(user.getJDAuser().getId()).getVoiceState().getChannel().asVoiceChannel());
            }
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("Searching and playing: \n`" + input + "`").queue();
        } else {
            guild.getGuild().getTextChannelById(replyChannel).sendMessage("You must be in a voice channel to play music!").queue();
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
