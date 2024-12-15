package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.manager.MusicManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicPlayerEntity {
    protected final MusicManager musicManager;
    private final List<AudioTrack> activePlaylist;
    private final List<AudioTrack> historyPlaylist;
    private int flagLoop = 1;
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

    public int getFlagLoop(){
        return this.flagLoop;
    }

    public MusicPlayerEntity toggleLoop(){
        switch (this.flagLoop) {
            case 1, 2:
                this.flagLoop++;
                break;
            case 3:
                this.flagLoop/=3;
                break;
        }
        return this;
    }

    public boolean isShuffle() {
        return this.flagShuffle;
    }

    public MusicPlayerEntity setShuffle(boolean flag) {
        this.flagShuffle = flag;
        return this;
    }

    public MusicPlayerEntity toggleShuffle() {
        this.flagShuffle = !this.flagShuffle;
        return this;
    }
}