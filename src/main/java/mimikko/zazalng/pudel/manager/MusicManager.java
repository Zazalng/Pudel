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
import mimikko.zazalng.pudel.entities.SessionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    protected final PudelWorld pudelWorld;
    private final AudioPlayerManager playerManager;

    public MusicManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true   // Allow direct playlist IDs
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager,com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public AudioPlayer musicManagerBuilder(){
        return this.playerManager.createPlayer();
    }

    public void loadAndPlay(SessionEntity session, String trackURL, Consumer<String> callback) {
        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                session.getGuild().getMusicPlayer().queueUp(track);
                String result = "accept:"+getTrackThumbnail(track);
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                String result;
                if (playlist.isSearchResult()) {
                    // If it's a search result, add the first track to the queue
                    session.addData("music.play.searching.top5",playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())));
                    result = trackURL;
                } else {
                    // Add all tracks in the playlist to the queue
                    session.getGuild().getMusicPlayer().queueUp(playlist);
                    result = "playlist:(<"+trackURL+">)";
                }
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void noMatches() {
                String result = "error:" + trackURL;
                callback.accept(result);  // Return the result via callback
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                String result = "error:" + trackURL;
                logger.error("LoadFailed", exception);
                callback.accept(result);  // Return the result via callback
            }
        });
    }

    public AudioTrack castAudioTrack(Object track){
        return (AudioTrack) track;
    }

    public String getTrackFormat(Object track) {
        return getTrackTitle(track) + " - " + getTrackUploader(track);
    }

    public String getTrackThumbnail(Object track){
        Matcher matcher = Pattern.compile("v=([^&]+)").matcher(castAudioTrack(track).getInfo().uri);
        return matcher.find() ? "https://img.youtube.com/vi/" + matcher.group(1) + "/maxresdefault.jpg" : "https://puu.sh/KgqvW.gif";
    }

    public String getTrackTitle(Object track){
        return castAudioTrack(track).getInfo().title;
    }

    public String getTrackUrl(Object track){
        return castAudioTrack(track).getInfo().uri;
    }

    public String getTrackUploader(Object track){
        return castAudioTrack(track).getInfo().author;
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
