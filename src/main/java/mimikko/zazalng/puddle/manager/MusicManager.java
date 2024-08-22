package mimikko.zazalng.puddle.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.handlers.AudioPlayerSendHandler;
import mimikko.zazalng.puddle.handlers.AudioTrackHandler;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static mimikko.zazalng.puddle.utility.IntegerUtility.randomInt;

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
                getGuildConnection(getGuild(), channel);
                queueUp(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                getGuildConnection(getGuild(), channel);
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

    private void queueUp(AudioTrack track){
        if(this.player.getAudioPlayer().getPlayingTrack() == null){
            this.playlist.add(track);
            this.player.getAudioPlayer().playTrack(trackSelection());
        } else{
            this.playlist.add(track);
            this.player.getAudioPlayer().startTrack(trackSelection(), true);
        }
    }

    private void queueUp(AudioPlaylist playlist){
        if(playlist.isSearchResult()){
            this.playlist.add(playlist.getTracks().get(0));
        } else{
            this.playlist.addAll(playlist.getTracks());
        }
        if(player.getAudioPlayer().getPlayingTrack() == null){
            this.player.getAudioPlayer().playTrack(trackSelection());
        }else{
            this.player.getAudioPlayer().startTrack(trackSelection(), true);
        }
    }

    private AudioTrack trackSelection(){
        if(!this.playlist.isEmpty()) {
            if(this.flagLoop && audioTrack != null){
                audioTrack = audioTrack.makeClone();
            }else if(this.flagLoop && audioTrack == null){
                audioTrack = this.playlist.get(0);
            }
            if(this.flagShuffle){
                int index = randomInt(this.playlist.size());
                audioTrack = this.playlist.get(index).makeClone();
                this.playlist.remove(index);
            } else{
                this.playlist.remove(0);
            }
            return audioTrack;
        } else{
            return null;
        }
    }

    public void getGuildConnection(GuildEntity guild, VoiceChannel channel){
        getGuild().getGuild().getAudioManager().openAudioConnection(channel);
        getGuild().getGuild().getAudioManager().setSendingHandler(this.player);
    }

    public void shufflePlaylist(){
        Collections.shuffle(this.playlist);
    }

    public void nextTrack(boolean isSkip) {
        if(isSkip){
            this.player.getAudioPlayer().playTrack(trackSelection());
        } else{
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
