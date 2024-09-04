package mimikko.zazalng.pudel.entities;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import java.util.List;

public class MusicPlayerEntity {
    private final AudioPlayerManager playerManager;
    private final List<AudioTrack> playlist;
    private AudioTrack audioTrack;
    private boolean flagLoop;
    private boolean flagShuffle;
}
