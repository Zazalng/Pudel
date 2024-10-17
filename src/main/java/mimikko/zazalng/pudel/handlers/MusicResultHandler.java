package mimikko.zazalng.pudel.handlers;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public class MusicResultHandler {
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

        // Getters and Setters
        public boolean isSuccess() {
            return this.success;
        }

        public MusicResultHandler setSuccess(boolean success) {
            this.success = success;
            return this;
        }

        public Type getType() {
            return this.type;
        }

        public MusicResultHandler setType(Type type) {
            this.type = type;
            return this;
        }

        public AudioTrack getTrack() {
            return this.track;
        }

        public MusicResultHandler setTrack(AudioTrack track) {
            this.track = track;
            return this;
        }

        public AudioPlaylist getPlaylist() {
            return this.playlist;
        }

        public MusicResultHandler setPlaylist(AudioPlaylist playlist) {
            this.playlist = playlist;
            return this;
        }

        public List<AudioTrack> getTopTracks() {
            return this.topTracks;
        }

        public MusicResultHandler setTopTracks(List<AudioTrack> topTracks) {
            this.topTracks = topTracks;
            return this;
        }
    }
