package mimikko.zazalng.pudel.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.handlers.AudioPlayerSendHandler;
import mimikko.zazalng.pudel.handlers.AudioTrackHandler;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicManager {
    private final GuildEntity guild;
    private final AudioPlayerManager playerManager;
    private final List<AudioTrack> playlist;
    private final AudioPlayerSendHandler player;
    private AudioTrack audioTrack;
    private boolean flagLoop;
    private boolean flagShuffle;

    public MusicManager(GuildEntity guild){
        this.guild = guild;
        this.flagLoop = false;
        this.flagShuffle = false;
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
        this.player = new AudioPlayerSendHandler(playerManager.createPlayer());
        this.player.getAudioPlayer().addListener(new AudioTrackHandler(this));
        this.playlist = new ArrayList<>();
        this.audioTrack = null;
    }

    public GuildEntity getGuild(){
        return this.guild;
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

    public void loadAndPlay(String trackUrl, VoiceChannel channel) {
        playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                getGuildConnection(channel);
                queueUp(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getGuildConnection(channel);
                queueUp(playlist);
            }

            @Override
            public void noMatches() {
                System.out.println("No track found for URL: " + trackUrl);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
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

    private void getGuildConnection(VoiceChannel channel){
        getGuild().getGuild().getAudioManager().openAudioConnection(channel);
        getGuild().getGuild().getAudioManager().setSendingHandler(this.player);
    }

    public String getTrackInfo(){
        return "["+this.player.getAudioPlayer().getPlayingTrack().getInfo().title+"](<"+this.player.getAudioPlayer().getPlayingTrack().getInfo().uri+">)";
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
        getGuild().getGuild().getAudioManager().setSendingHandler(null);
        getGuild().getGuild().getAudioManager().closeAudioConnection();
    }
}
