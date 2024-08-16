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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class MusicManager {
    private final AudioPlayerManager playerManager;

    public MusicManager(){
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public void loadAndPlay(Guild guild, String trackUrl, VoiceChannel channel) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {

            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

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

    public getGuildConnection(Guild guild, VoiceChannel channel){
        guild.getAudioManager().openAudioConnection(channel);
        guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
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
        if(playlist.isSearchResult()){
            this.playlist.add(playlist.getTracks().get(0));
        } else{
            this.playlist.addAll(playlist.getTracks());
        }
        if(player.getPlayingTrack() == null){
            player.playTrack(this.playlist.get(0));
        }else{
            player.startTrack(this.playlist.get(0), true);
        }
    }

    public void nextTrack() {
        this.playlist.remove(0);
        if(!this.playlist.isEmpty()){
            player.startTrack(this.playlist.get(0), true);
        }
    }

    public void stop(Guild guild) {
        playlist.clear();
        player.stopTrack();
        player.destroy();
        guild.getAudioManager().closeAudioConnection();
    }

    private void connectToVoiceChannel(Guild guild, VoiceChannel channel) {

    }
}
