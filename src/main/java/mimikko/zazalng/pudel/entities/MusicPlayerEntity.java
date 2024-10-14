package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioPlayerSendHandler;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioTrackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicPlayerEntity {
    private final List<AudioTrack> playlist;
    private AudioPlayerSendHandler player;
    private AudioTrack audioTrack;
    private boolean flagLoop;
    private boolean flagShuffle;

    public MusicPlayerEntity(AudioPlayer playerManager){
        this.playlist = new ArrayList<>();
        this.player = new AudioPlayerSendHandler(playerManager);
        this.player.getAudioPlayer().addListener(new AudioTrackHandler(this));
    }

    public List<AudioTrack> getPlaylist(){
        return this.playlist;
    }

    public AudioPlayerSendHandler getPlayer() {
        return player;
    }

    public void queueUp(AudioTrack track) {
        if (this.player.getAudioPlayer().getPlayingTrack() == null) {
            getPlaylist().add(track);
            this.audioTrack = track;
            this.player.getAudioPlayer().playTrack(trackSelection());
        } else {
            getPlaylist().add(track);
        }
    }

    public void queueUp(AudioPlaylist playlist) {
        getPlaylist().addAll(playlist.getTracks());

        if (this.player.getAudioPlayer().getPlayingTrack() == null) {
            this.player.getAudioPlayer().playTrack(trackSelection());
        }
    }

    public AudioTrack getPlayingTrack(){
        if(this.player.getAudioPlayer().getPlayingTrack() == null){
            return null;
        }
        return this.player.getAudioPlayer().getPlayingTrack();
    }

    private AudioTrack trackSelection() {
        if (flagLoop && audioTrack != null) {
            // If looping is enabled, replay the current track
            return audioTrack.makeClone();
        } else if (flagShuffle && !getPlaylist().isEmpty()) {
            // If shuffle is enabled, pick a random track from the playlist
            int index = randomInt(getPlaylist().size());
            AudioTrack selectedTrack = getPlaylist().remove(index);
            this.audioTrack = selectedTrack.makeClone();
            return this.audioTrack;
        } else if (!getPlaylist().isEmpty()) {
            // Play the next track in the playlist
            AudioTrack nextTrack = getPlaylist().remove(0);
            this.audioTrack = nextTrack.makeClone();
            return this.audioTrack;
        } else {
            return null; // No track to play
        }
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
        this.player = null;
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
