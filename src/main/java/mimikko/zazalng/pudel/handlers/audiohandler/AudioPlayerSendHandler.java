package mimikko.zazalng.pudel.handlers.audiohandler;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    public AudioPlayer getAudioPlayer(){
        return this.audioPlayer;
    }

    @Override
    public boolean canProvide() {
        this.lastFrame = getAudioPlayer().provide();
        return this.lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return ByteBuffer.wrap(this.lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true; // Lavaplayer provides audio in Opus format, which JDA uses.
    }
}