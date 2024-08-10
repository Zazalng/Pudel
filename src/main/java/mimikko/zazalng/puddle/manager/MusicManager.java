package mimikko.zazalng.puddle.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.puddle.handlers.AudioPlayerSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class MusicManager {
    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;

    public MusicManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        this.player = playerManager.createPlayer();
    }

    public void loadAndPlay(Guild guild, String trackUrl, VoiceChannel channel) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                connectToVoiceChannel(guild, channel);
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                // Handle playlists, but for now, let's just play the first track.
                trackLoaded(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                System.out.println("No track found for URL: " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }

    private void connectToVoiceChannel(Guild guild, VoiceChannel channel) {
        guild.getAudioManager().openAudioConnection(channel);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
    }

    public void stop(Guild guild) {
        player.stopTrack();
        guild.getAudioManager().closeAudioConnection();
    }
}
