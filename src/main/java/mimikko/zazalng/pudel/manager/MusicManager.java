package mimikko.zazalng.pudel.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;

public class MusicManager implements Manager {
    protected final PudelWorld pudelWorld;
    private final AudioPlayerManager playerManager;

    public MusicManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager,com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public AudioPlayer musicManagerBuilder(){
        return this.playerManager.createPlayer();
    }

    public void loadAndPlay(MusicPlayerEntity player, String trackUrl) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.queueUp(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                player.queueUp(playlist);
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

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
