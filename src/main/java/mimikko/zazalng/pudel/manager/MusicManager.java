package mimikko.zazalng.pudel.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.*;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import mimikko.zazalng.pudel.handlers.MusicResultHandler;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioPlayerSendHandler;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioTrackHandler;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mimikko.zazalng.pudel.utility.IntegerUtility.randomInt;

public class MusicManager extends AbstractManager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    private final AudioPlayerManager playerManager;
    private final Map<GuildEntity,MusicPlayerEntity> playerList;
    private final Map<MusicPlayerEntity, AudioPlayerSendHandler> audioList;

    protected MusicManager(PudelWorld pudelWorld) {
        super(pudelWorld);
        this.playerList = new HashMap<>();
        this.audioList = new HashMap<>();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true,    // Allow direct playlist IDs
                new Music(),
                new Web());
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager, com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    private AudioPlayer musicManagerBuilder(MusicPlayerEntity e) {
        AudioPlayer audioPlayer = this.playerManager.createPlayer();
        audioPlayer.addListener(new AudioTrackHandler(e));
        return audioPlayer;
    }

    public <T extends InteractionEntity> MusicPlayerEntity getMusicPlayer(T session){
        return this.playerList.computeIfAbsent(session.getGuild(), player -> new MusicPlayerEntity(this));
    }

    private AudioPlayerSendHandler getAudioPlayer(MusicPlayerEntity e){
        return this.audioList.computeIfAbsent(e, audio -> new AudioPlayerSendHandler(musicManagerBuilder(e)));
    }

    public MusicManager stopPlayer(TextEntity session){
        getPudelManager().closeVoiceConnection(session).getMusicManager().getPlayer(session).stopTrack();
        return this;
    }

    public MusicManager stopPlayer(ReactionEntity interaction){
        getPudelManager().closeVoiceConnection(interaction).getMusicManager().getPlayer(interaction).stopTrack();
        return this;
    }

    /**
     * Loads and plays a track or playlist based on the given track URL.
     * Returns a result object to inform the calling command about the outcome.
     */
    public CompletableFuture<MusicResultHandler> loadAndPlay(TextEntity session, String trackURL) {
        CompletableFuture<MusicResultHandler> resultFuture = new CompletableFuture<>();
        MusicResultHandler result = new MusicResultHandler(trackURL);

        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                track.setUserData(session.getUser());
                result.setTrack(track)
                        .setType(MusicResultHandler.Type.TRACK);
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    result.setTopTracks(playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())))
                            .getTopTracks().forEach(track -> track.setUserData(session.getUser()));
                    result.setType(MusicResultHandler.Type.SEARCH);
                } else {
                    playlist.getTracks().forEach(track -> track.setUserData(session.getUser()));
                    result.setPlaylist(playlist)
                            .setType(MusicResultHandler.Type.PLAYLIST);
                }
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void noMatches() {
                result.setType(MusicResultHandler.Type.NO_MATCH);
                resultFuture.complete(result);  // Complete the future with the result
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                result.setType(MusicResultHandler.Type.LOAD_FAILED);
                logger.error("LoadFailed", exception);
                resultFuture.complete(result);  // Complete the future with the result
            }
        });

        return resultFuture;
    }


    public MusicManager loadAndPlay(TextEntity session, MusicResultHandler result){
        if(result.getType() == MusicResultHandler.Type.TRACK){
            queueUp(getMusicPlayer(session),result.getTrack()).nextTrack(session,false);
        }else{
            queueUp(getMusicPlayer(session),result.getPlaylist().getTracks()).nextTrack(session,false);
        }
        return this;
    }

    public MusicManager loadAndPlay(ReactionEntity interaction, Object track){
        queueUp(getMusicPlayer(interaction),castAudioTrack(track)).nextTrack(interaction, false);
        return this;
    }

    // Utility methods for track information
    public String getTrackFormat(Object track) {
        return getTrackTitle(track) + " - " + getTrackUploader(track);
    }

    public String getTrackThumbnail(Object track) {
        Matcher matcher = Pattern.compile("v=([^&]+)").matcher(castAudioTrack(track).getInfo().uri);
        return matcher.find() ? "https://img.youtube.com/vi/" + matcher.group(1) + "/maxresdefault.jpg" : "https://puu.sh/KgqvW.gif";
    }

    public String getTrackTitle(Object track) {
        return castAudioTrack(track).getInfo().title;
    }

    public String getTrackUrl(Object track) {
        return castAudioTrack(track).getInfo().uri;
    }

    public String getTrackUploader(Object track) {
        return castAudioTrack(track).getInfo().author;
    }

    public String getTrackDuration(Object track) {
        return String.format("%s / %s", castDuration(castAudioTrack(track).getPosition()), castDuration(castAudioTrack(track).getDuration()));
    }

    private String castDuration(long duration) {
        long seconds = duration / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private AudioTrack castAudioTrack(Object track) {
        return (AudioTrack) track;
    }

    public String getLoopKey(TextEntity session){
        return getLoopKey(getMusicPlayer(session).getFlagLoop());
    }

    public String getLoopKey(ReactionEntity interaction){
        return getLoopKey(getMusicPlayer(interaction).getFlagLoop());
    }

    private String getLoopKey(int flagLoop){
        return switch (flagLoop) {
            case 1 -> "music.loop.off";
            case 2 -> "music.loop.playlist";
            case 3 -> "music.loop.track";
            default -> "music.loop.error";
        };
    }

    private AudioPlayer getPlayer(TextEntity session){
        return getPlayer(getMusicPlayer(session));
    }

    private AudioPlayer getPlayer(ReactionEntity interaction){
        return getPlayer(getMusicPlayer(interaction));
    }

    private AudioPlayer getPlayer(MusicPlayerEntity e){
        return getAudioPlayer(e).getAudioPlayer();
    }

    private AudioTrack trackSelection(MusicPlayerEntity e){
        if(e.getActivePlaylist().isEmpty()){
            return null;
        } else if(e.isShuffle()){
            return e.getActivePlaylist().get(randomInt(e.getActivePlaylist().size()));
        } else{
            return e.getActivePlaylist().getFirst();
        }
    }

    public AudioTrack getPlayingTrack(TextEntity session){
        return getPlayer(session).getPlayingTrack();
    }

    private AudioTrack getPlayingTrack(MusicPlayerEntity e){
        return getPlayer(e).getPlayingTrack();
    }

    private MusicManager queueUp(MusicPlayerEntity entity, AudioTrack track) {
        entity.getActivePlaylist().add(track);
        return this;
    }

    private MusicManager queueUp(MusicPlayerEntity entity, List<AudioTrack> playlist) {
        entity.getActivePlaylist().addAll(playlist);
        return this;
    }

    public MusicManager nextTrack(TextEntity session, boolean isSkip){
        session.getManager().getPudelManager().openVoiceConnection(session,getAudioPlayer(getMusicPlayer(session)));
        nextTrack(getMusicPlayer(session),isSkip);
        return this;
    }

    public MusicManager nextTrack(ReactionEntity interaction, boolean isSkip){
        interaction.getManager().getPudelManager().openVoiceConnection(interaction,getAudioPlayer(getMusicPlayer(interaction)));
        nextTrack(getMusicPlayer(interaction),isSkip);
        return this;
    }

    public MusicManager nextTrack(MusicPlayerEntity e, AudioTrack track){
        getPlayer(e).startTrack(track,true);
        return this;
    }

    public MusicManager nextTrack(MusicPlayerEntity e, boolean isSkip) {
        if (isSkip || getPlayingTrack(e)==null) {
            getPlayer(e).playTrack(trackSelection(e));
        } else {
            nextTrack(e,trackSelection(e));
        }
        return this;
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}
