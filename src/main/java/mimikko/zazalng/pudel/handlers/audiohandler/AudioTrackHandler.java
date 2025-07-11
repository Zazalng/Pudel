package mimikko.zazalng.pudel.handlers.audiohandler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;

public class AudioTrackHandler extends AudioEventAdapter {
    protected MusicPlayerEntity player;

    public AudioTrackHandler(MusicPlayerEntity player) {
        this.player = player;
    }

    private MusicPlayerEntity getMusicEntity(){
        return this.player;
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        // Player was paused
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        // Player was resumed
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // A track started playing
        getMusicEntity().getActivePlaylist().remove(track);
        getMusicEntity().getHistoryPlaylist().addFirst(track);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason == AudioTrackEndReason.FINISHED) {
            // Start next track
            getMusicEntity().getMusicManager().nextTrack(getMusicEntity(),false);
        } else if(endReason == AudioTrackEndReason.STOPPED){

        } else if(endReason == AudioTrackEndReason.REPLACED){
            player.stopTrack();
            getMusicEntity().getMusicManager().nextTrack(getMusicEntity(),false);
        } else if(endReason == AudioTrackEndReason.CLEANUP){

        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
        //                       clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        player.playTrack(track.makeClone());
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }
}