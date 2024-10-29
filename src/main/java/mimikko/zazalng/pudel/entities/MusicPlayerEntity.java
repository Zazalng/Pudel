package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.manager.MusicManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicPlayerEntity {
    private MusicManager musicManager;
    private final List<AudioTrack> activePlaylist;
    private final List<AudioTrack> historyPlaylist;
    private boolean flagLoop;
    private boolean flagShuffle;

    public MusicPlayerEntity(MusicManager musicManager){
        this.musicManager = musicManager;
        this.activePlaylist = new ArrayList<>();
        this.historyPlaylist = new ArrayList<>();
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public List<AudioTrack> getActivePlaylist(){
        return this.activePlaylist;
    }

    public List<AudioTrack> getHistoryPlaylist(){
        return this.historyPlaylist;
    }

    public MusicPlayerEntity shufflePlaylist(){
        Collections.shuffle(this.activePlaylist);
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
