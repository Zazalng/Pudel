package mimikko.zazalng.puddle.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.puddle.handlers.AudioPlayerSendHandler;
import mimikko.zazalng.puddle.handlers.AudioTrackHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private final AudioPlayerManager playerManager;
    private final AudioPlayer player;
    private final List<AudioTrack> playlist;

    public MusicManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        this.player = playerManager.createPlayer();
        this.playlist = new ArrayList<>();
        this.player.addListener(new AudioTrackHandler(player,this));
    }

    public void loadAndPlay(Guild guild, String trackUrl, VoiceChannel channel) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                connectToVoiceChannel(guild, channel);
                queueUp(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                connectToVoiceChannel(guild, channel);
                queueUp(playlist);
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

    private void queueUp(AudioTrack track){
        if(player.getPlayingTrack() == null){
            playlist.add(track);
            player.playTrack(playlist.get(0));
        } else{
            playlist.add(track);
            player.startTrack(playlist.get(0), true);
        }
    }

    private void queueUp(AudioPlaylist playlist){
        if(player.getPlayingTrack() == null){
            this.playlist.addAll(playlist.getTracks());
            player.playTrack(this.playlist.get(0));
        }else{
            this.playlist.addAll(playlist.getTracks());
            player.startTrack(this.playlist.get(0), true);
        }
    }

    public void nextTrack() {
        this.playlist.remove(0);
        if(this.playlist.isEmpty()){

        }else{
            player.startTrack(this.playlist.get(0), true);
        }
    }

    public void stop(Guild guild) {
        player.stopTrack();
        guild.getAudioManager().closeAudioConnection();
    }

    private void connectToVoiceChannel(Guild guild, VoiceChannel channel) {
        guild.getAudioManager().openAudioConnection(channel);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
    }
}
