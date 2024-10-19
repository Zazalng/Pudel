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
import dev.lavalink.youtube.clients.*;
import dev.lavalink.youtube.clients.skeleton.Client;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.handlers.MusicResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    protected final PudelWorld pudelWorld;
    private final AudioPlayerManager playerManager;

    public MusicManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true,    // Allow direct playlist IDs
                new Client[]
                        {
                                new Music(),
                                new Web(),
                                new WebEmbedded()
                        }
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public AudioPlayer musicManagerBuilder() {
        return this.playerManager.createPlayer();
    }

    /**
     * Loads and plays a track or playlist based on the given track URL.
     * Returns a result object to inform the calling command about the outcome.
     */
    public CompletableFuture<MusicResultHandler> loadAndPlay(SessionEntity session, String trackURL) {
        CompletableFuture<MusicResultHandler> resultFuture = new CompletableFuture<>();
        MusicResultHandler result = new MusicResultHandler(trackURL);

        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(session.getUser());
                result.setTrack(track)
                        .setType(MusicResultHandler.Type.TRACK);
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    result.setTopTracks(playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())))
                            .setType(MusicResultHandler.Type.SEARCH);
                } else {
                    playlist.getTracks().forEach(track -> track.setUserData(session.getUser()));
                    result.setPlaylist(playlist)
                            .setType(MusicResultHandler.Type.PLAYLIST);
                }
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void noMatches() {
                result.setType(MusicResultHandler.Type.NO_MATCH);
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                result.setType(MusicResultHandler.Type.LOAD_FAILED);
                logger.error("LoadFailed", exception);
                resultFuture.complete(result);  // Complete the future with the result
            }
        });

        return resultFuture;
    }


    public MusicManager loadAndPlay(SessionEntity session, MusicResultHandler result){
        getPudelWorld().getPudelManager().openVoiceConnection(session);
        if(result.getType() == MusicResultHandler.Type.TRACK){
            session.getGuild().getMusicPlayer().queueUp(result.getTrack()).nextTrack(false);
        }else{
            session.getGuild().getMusicPlayer().queueUp(result.getPlaylist().getTracks()).nextTrack(false);
        }
        return this;
    }

    // Utility methods for track information
    public String getTrackFormat(Object track) {
        return getTrackTitle(track) + " - " + getTrackUploader(track);
    }

    public String getTrackThumbnail(Object track) {
        Matcher matcher = Pattern.compile("v=([^&]+)").matcher(castAudioTrack(track).getInfo().uri);
        return matcher.find() ? "https://img.youtube.com/vi/" + matcher.group(1) + "/maxresdefault.jpg" : "https://puu.sh/KgqvW.gif";
    }

    public String getTrackTitle(Object track) {
        return castAudioTrack(track).getInfo().title;
    }

    public String getTrackUrl(Object track) {
        return castAudioTrack(track).getInfo().uri;
    }

    public String getTrackUploader(Object track) {
        return castAudioTrack(track).getInfo().author;
    }

    public String getTrackDuration(Object track) {
        return String.format("%s / %s", castDuration(castAudioTrack(track).getPosition()), castDuration(castAudioTrack(track).getDuration()));
    }

    public String castDuration(long duration) {
        long seconds = duration / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public AudioTrack castAudioTrack(Object track) {
        return (AudioTrack) track;
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public MusicManager initialize() {
        return this;
    }

    @Override
    public MusicManager reload() {
        return this;
    }

    @Override
    public MusicManager shutdown() {
        getPudelWorld().getGuildManager().getGuildEntity().forEach(guild -> guild.getMusicPlayer().stop());
        return this;
    }
}
