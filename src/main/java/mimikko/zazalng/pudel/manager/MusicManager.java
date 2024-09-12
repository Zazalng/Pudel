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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class MusicManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
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

    public void loadAndPlay(MusicPlayerEntity player, String trackURL, Consumer<String> callback) {
        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                player.queueUp(track);
                String result = "Search & Play result.\n" + getTrackInfo(track);
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                String result;
                if (playlist.isSearchResult()) {
                    // If it's a search result, add the first track to the queue
                    player.queueUp(playlist.getTracks().get(0));
                    result = "Search & Play result.\n" + getTrackInfo(playlist.getTracks().get(0));
                } else {
                    // Add all tracks in the playlist to the queue
                    player.queueUp(playlist);
                    result = "Playlist had loaded successfully.\n[Link Playlist](<" + trackURL + ">)";
                }
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void noMatches() {
                String result = "No Match searching / Invalid Input.\n`" + trackURL + "`";
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                String result = "Load failed due to an error.\n`" + trackURL + "`";
                logger.error("LoadFailed", exception);
                callback.accept(result);  // Return the result via callback
            }
        });
    }

    public String getTrackInfo(AudioTrack track) {
        return "[" + track.getInfo().title + "](<" + track.getInfo().uri + ">)";
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
