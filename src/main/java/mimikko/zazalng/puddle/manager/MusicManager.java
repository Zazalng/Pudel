package mimikko.zazalng.puddle.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.handlers.AudioPlayerSendHandler;
import mimikko.zazalng.puddle.handlers.AudioTrackHandler;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;

public class MusicManager {
    private final GuildEntity guild;
    private final AudioPlayerManager playerManager;
    private final List<AudioTrack> playlist;
    private final AudioPlayerSendHandler player;

    public MusicManager(GuildEntity guild){
        this.guild = guild;
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        this.player = new AudioPlayerSendHandler(playerManager.createPlayer());
        this.player.getAudioPlayer().addListener(new AudioTrackHandler(this));
        this.playlist = new ArrayList<>();
    }

    public GuildEntity getGuild(){
        return this.guild;
    }

    public void loadAndPlay(String trackUrl, VoiceChannel channel) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getGuildConnection(getGuild(), channel);
                queueUp(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getGuildConnection(getGuild(), channel);
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
    public void getGuildConnection(GuildEntity guild, VoiceChannel channel){
        guild.getGuild().getAudioManager().openAudioConnection(channel);
        guild.getGuild().getAudioManager().setSendingHandler(this.player);
    }

    private void queueUp(AudioTrack track){
        if(player.getAudioPlayer().getPlayingTrack() == null){
            this.playlist.add(track);
            player.getAudioPlayer().playTrack(playlist.get(0));
        } else{
            playlist.add(track);
            player.getAudioPlayer().startTrack(playlist.get(0), true);
        }
    }

    private void queueUp(AudioPlaylist playlist){
        if(playlist.isSearchResult()){
            this.playlist.add(playlist.getTracks().get(0));
        } else{
            this.playlist.addAll(playlist.getTracks());
        }
        if(player.getAudioPlayer().getPlayingTrack() == null){
            player.getAudioPlayer().playTrack(this.playlist.get(0));
        }else{
            player.getAudioPlayer().startTrack(this.playlist.get(0), true);
        }
    }

    public void nextTrack() {
        this.playlist.remove(0);
        if(!this.playlist.isEmpty()){
            player.getAudioPlayer().startTrack(this.playlist.get(0), true);
        }
    }

    public void stop() {
        playlist.clear();
        player.getAudioPlayer().stopTrack();
        player.getAudioPlayer().destroy();
        guild.getGuild().getAudioManager().setSendingHandler(null);
        guild.getGuild().getAudioManager().closeAudioConnection();
    }
}
