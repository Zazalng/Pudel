package mimikko.zazalng.puddle.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.audio.AudioSendHandler;

import javax.sound.sampled.AudioFormat;
import java.nio.ByteBuffer;

public class AudioPlayerSendHandler implements AudioSendHandler {
    private final AudioPlayer audioPlayer;
    private final ByteBuffer buffer;
    private final AudioFormat format;

    public AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.buffer = ByteBuffer.allocate(1024);
        this.format = new AudioFormat(48000, 16, 2, true, true);
    }

    @Override
    public boolean canProvide() {
        return audioPlayer.provide().isTerminator();
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true; // Lavaplayer provides audio in Opus format, which JDA uses.
    }
}