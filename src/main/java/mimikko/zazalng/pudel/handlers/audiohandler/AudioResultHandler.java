package mimikko.zazalng.pudel.handlers.audiohandler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;

public class AudioResultHandler implements AudioLoadResultHandler {
    private MusicPlayerEntity player;
    private String result;

    public AudioResultHandler(MusicPlayerEntity player){
        this.player = player;
        result = "";
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        player.queueUp(track);
        result = player.getTrackInfo(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        result = player.queueUp(playlist);
    }

    @Override
    public void noMatches() {
        result = "NULL";
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        result = exception.getMessage();
    }

    public String getResult() {
        return result;
    }
}
