package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.handlers.AudioPlayerSendHandler;
import mimikko.zazalng.pudel.handlers.AudioTrackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicPlayerEntity {
    private final List<AudioTrack> playlist;
    private final AudioPlayerSendHandler player;
    private AudioTrack audioTrack;
    private boolean flagLoop;
    private boolean flagShuffle;

    public MusicPlayerEntity(AudioPlayer playerManager){
        this.playlist = new ArrayList<>();
        this.player = new AudioPlayerSendHandler(playerManager);
        this.player.getAudioPlayer().addListener(new AudioTrackHandler(this));
    }

    private void queueUp(AudioTrack track) {
        if (this.player.getAudioPlayer().getPlayingTrack() == null) {
            this.playlist.add(track);
            this.audioTrack = track;
            this.player.getAudioPlayer().playTrack(trackSelection());
        } else {
            this.playlist.add(track);
        }
    }

    private void queueUp(AudioPlaylist playlist) {
        if (playlist.isSearchResult()) {
            // If it's a search result, add the first track to the queue
            this.playlist.add(playlist.getTracks().get(0));
        } else {
            // Add all tracks in the playlist to the queue
            this.playlist.addAll(playlist.getTracks());
        }

        // Play the first track if none is currently playing
        if (this.player.getAudioPlayer().getPlayingTrack() == null) {
            this.player.getAudioPlayer().playTrack(trackSelection());
        }
    }

    public String getTrackInfo(){
        return "["+this.player.getAudioPlayer().getPlayingTrack().getInfo().title+"](<"+this.player.getAudioPlayer().getPlayingTrack().getInfo().uri+">)";
    }

    private AudioTrack trackSelection() {
        if (flagLoop && audioTrack != null) {
            // If looping is enabled, replay the current track
            return audioTrack.makeClone();
        } else if (flagShuffle && !playlist.isEmpty()) {
            // If shuffle is enabled, pick a random track from the playlist
            int index = randomInt(playlist.size());
            AudioTrack selectedTrack = playlist.remove(index);
            this.audioTrack = selectedTrack.makeClone();
            return this.audioTrack;
        } else if (!playlist.isEmpty()) {
            // Play the next track in the playlist
            AudioTrack nextTrack = playlist.remove(0);
            this.audioTrack = nextTrack.makeClone();
            return this.audioTrack;
        } else {
            return null; // No track to play
        }
    }

    public AudioPlayerSendHandler getAudioPlayer(){
        return this.player;
    }

    public void shufflePlaylist(){
        Collections.shuffle(this.playlist);
    }

    public void nextTrack(boolean isSkip) {
        if (isSkip || flagLoop) {
            this.player.getAudioPlayer().playTrack(trackSelection());
        } else {
            this.player.getAudioPlayer().startTrack(trackSelection(), true);
        }
    }

    public void stop() {
        this.playlist.clear();
        this.player.getAudioPlayer().stopTrack();
        this.player.getAudioPlayer().destroy();
    }

    public boolean isLoop(){
        return this.flagLoop;
    }

    public void setLoop(boolean flag){
        this.flagLoop = flag;
    }

    public boolean isShuffle() {
        return this.flagShuffle;
    }

    public void setShuffle(boolean flag) {
        this.flagShuffle = flag;
    }
}
