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

import java.util.List;
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
                true    // Allow direct playlist IDs
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public class MusicLoadResult {

        public enum Type {
            TRACK,       // A single track was loaded
            PLAYLIST,    // A playlist was loaded
            SEARCH,      // Search results were returned
            NO_MATCH,    // No match found for the input
            LOAD_FAILED  // An error occurred during loading
        }

        private boolean success;           // Indicates if the load was successful
        private Type type;                 // The type of result (TRACK, PLAYLIST, etc.)
        private AudioTrack track;          // The loaded track (if any)
        private AudioPlaylist playlist;    // The loaded playlist (if any)
        private List<AudioTrack> topTracks; // The top search results (if it's a search result)
        private String message;            // An optional message (e.g., for errors)

        // Getters and Setters
        public boolean isSuccess() {
            return success;
        }

        public MusicLoadResult setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Type getType() {
            return type;
        }

        public MusicLoadResult setType(Type type) {
            this.type = type;
            return this;
        }

        public AudioTrack getTrack() {
            return track;
        }

        public MusicLoadResult setTrack(AudioTrack track) {
            this.track = track;
            return this;
        }

        public AudioPlaylist getPlaylist() {
            return playlist;
        }

        public MusicLoadResult setPlaylist(AudioPlaylist playlist) {
            this.playlist = playlist;
            return this;
        }

        public List<AudioTrack> getTopTracks() {
            return topTracks;
        }

        public MusicLoadResult setTopTracks(List<AudioTrack> topTracks) {
            this.topTracks = topTracks;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public MusicLoadResult setMessage(String message) {
            this.message = message;
            return this;
        }
    }

    public AudioPlayer musicManagerBuilder() {
        return this.playerManager.createPlayer();
    }

    /**
     * Loads and plays a track or playlist based on the given track URL.
     * Returns a result object to inform the calling command about the outcome.
     */
    public MusicLoadResult loadAndPlay(SessionEntity session, String trackURL) {
        MusicLoadResult result = new MusicLoadResult();

        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(session.getUser());
                session.getGuild().getMusicPlayer().queueUp(track);

                // Prepare result with track information
                result.setTrack(track)
                        .setSuccess(true)
                        .setType(MusicLoadResult.Type.TRACK);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    // Search result, return the top 5 tracks for further selection
                    session.addData("music.play.searching.top5", playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())));

                    result.setTopTracks(playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())))
                            .setSuccess(true)
                            .setType(MusicLoadResult.Type.SEARCH);
                } else {
                    // Full playlist
                    session.getGuild().getMusicPlayer().queueUp(playlist.getTracks().stream()
                            .peek(track -> track.setUserData(session.getUser())).toList());

                    result.setPlaylist(playlist)
                            .setSuccess(true)
                            .setType(MusicLoadResult.Type.PLAYLIST);
                }
            }

            @Override
            public void noMatches() {
                result.setSuccess(false)
                        .setType(MusicLoadResult.Type.NO_MATCH)
                        .setMessage("No matches found for: " + trackURL);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                result.setSuccess(false)
                        .setType(MusicLoadResult.Type.LOAD_FAILED)
                        .setMessage("Failed to load track: " + exception.getMessage());
                logger.error("LoadFailed", exception);
            }
        });

        return result;
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
