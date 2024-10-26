package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioPlayerSendHandler;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioTrackHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicPlayerEntity {
    private final List<AudioTrack> activePlaylist;
    private final List<AudioTrack> historyPlaylist;
    private final AudioPlayerSendHandler player;
    private boolean flagLoop;
    private boolean flagShuffle;

    public MusicPlayerEntity(AudioPlayer player){
        this.activePlaylist = new ArrayList<>();
        this.historyPlaylist = new ArrayList<>();
        this.player = new AudioPlayerSendHandler(player);
        this.player.getAudioPlayer().addListener(new AudioTrackHandler(this));
    }

    public List<AudioTrack> getActivePlaylist(){
        return this.activePlaylist;
    }

    public List<AudioTrack> getHistoryPlaylist(){
        return this.historyPlaylist;
    }

    public AudioPlayerSendHandler getPlayer() {
        return player;
    }

    public MusicPlayerEntity queueUp(AudioTrack track) {
        getActivePlaylist().add(track);
        return this;
    }

    public MusicPlayerEntity queueUp(List<AudioTrack> playlist) {
        getActivePlaylist().addAll(playlist);
        return this;
    }

    public AudioTrack getPlayingTrack(){
        if(getPlayer().getAudioPlayer().getPlayingTrack() == null){
            return null;
        }
        return getPlayer().getAudioPlayer().getPlayingTrack();
    }

    public MusicPlayerEntity nextTrack(boolean isSkip) {
        if (getPlayer().getAudioPlayer().getPlayingTrack()==null) {
            getPlayer().getAudioPlayer().playTrack(trackSelection());
        } else if(isSkip){
            getPlayer().getAudioPlayer().stopTrack();
        } else {
            getPlayer().getAudioPlayer().startTrack(trackSelection(), true);
        }
        return this;
    }

    private AudioTrack trackSelection() {
        if(flagShuffle){
            return getActivePlaylist().get(randomInt(getActivePlaylist().size()));
        } else if(!getActivePlaylist().isEmpty()){
            return getActivePlaylist().getFirst();
        } else{
            return null;
        }
    }

    public MusicPlayerEntity shufflePlaylist(){
        Collections.shuffle(this.activePlaylist);
        return this;
    }

    public MusicPlayerEntity stop() {
        getActivePlaylist().clear();
        getPlayer().getAudioPlayer().stopTrack();
        return this;
    }

    public boolean isLoop(){
        return this.flagLoop;
    }

    public MusicPlayerEntity setLoop(boolean flag){
        this.flagLoop = flag;
        return this;
    }

    public boolean isShuffle() {
        return this.flagShuffle;
    }

    public MusicPlayerEntity setShuffle(boolean flag) {
        this.flagShuffle = flag;
        return this;
    }
}
