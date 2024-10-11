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
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static mimikko.zazalng.pudel.utility.ListUtility.getListObject;
import static mimikko.zazalng.pudel.utility.ListUtility.getListSize;

public class MusicManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    protected final PudelWorld pudelWorld;
    private final AudioPlayerManager playerManager;

    public MusicManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true   // Allow direct playlist IDs
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager,com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public AudioPlayer musicManagerBuilder(){
        return this.playerManager.createPlayer();
    }

    public MusicManager loadAndPlay(SessionEntity session, String trackURL) {
        playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                session.getGuild().getMusicPlayer().queueUp(track);
                session.getChannel().sendMessageEmbeds(
                        getPudelWorld().getEmbedManager().createEmbed(session)
                                .setTitle(getTrackFormat(track),track.getInfo().uri)
                                .setDescription(getPudelWorld().getLocalizationManager().getLocalizedText(session,"music.manager.accept",null))
                                .setThumbnail(getTrackThumbnail(track)).build())
                        .queue();
                session.setState("END");
                getPudelWorld().getPudelManager().OpenVoiceConnection(session).setSendingHandler(session);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                if (playlist.isSearchResult()) {
                    // If it's a search result, add the first track to the queue
                    session.addData("music.play.searching.top5",playlist.getTracks().subList(0, Math.min(5, playlist.getTracks().size())));
                    session.getChannel().sendMessageEmbeds(
                            getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(String.format("%s\n%s",getPudelWorld().getLocalizationManager().getLocalizedText(session, "music.manager.searching",null),trackURL))
                                    .setThumbnail("https://puu.sh/KgdPy.gif")
                                    .setDescription(getSongList(session))
                                    .build())
                            .queue();
                    session.setState("MUSIC.PLAY.SEARCHING");
                } else {
                    // Add all tracks in the playlist to the queue
                    session.getGuild().getMusicPlayer().queueUp(playlist);
                    session.getChannel().sendMessageEmbeds(
                            getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(getPudelWorld().getLocalizationManager().getLocalizedText(session, "music.manager.playlist",null),trackURL)
                                    .setThumbnail("https://puu.sh/KgxX3.gif")
                                    .build())
                            .queue();
                    session.setState("END");
                }
                getPudelWorld().getPudelManager().OpenVoiceConnection(session).setSendingHandler(session);
            }

            @Override
            public void noMatches() {
                session.getChannel().sendMessageEmbeds(
                        getPudelWorld().getEmbedManager().createEmbed(session)
                                .setTitle(getPudelWorld().getLocalizationManager().getLocalizedText(session, "music.manager.nomatch",null))
                                .setDescription(String.format("`%s`",trackURL))
                                .setThumbnail("https://puu.sh/KgAxi.gif")
                                .build())
                        .queue();
                session.setState("END");
                getPudelWorld().getPudelManager().OpenVoiceConnection(session).setSendingHandler(session);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                session.getChannel().sendMessageEmbeds(
                        getPudelWorld().getEmbedManager().createEmbed(session)
                                .setColor((255 << 16) | (0 << 8) | 0)
                                .setTitle(getPudelWorld().getLocalizationManager().getLocalizedText(session, "music.manager.exception",null))
                                .setDescription(String.format("`%s`\n```%s```",trackURL,exception.toString()))
                                .setThumbnail("https://puu.sh/KgAxn.gif")
                                .build())
                        .queue();
                logger.error("LoadFailed", exception);
                session.setState("END");
            }
        });
        return this;
    }

    private String getSongList(SessionEntity session){
        StringBuilder str = new StringBuilder();
        int size = getListSize(session.getData("music.play.searching.top5",false));
        for(int i = 0; i<size;i++){
            str.append("[").append(i+1).append(". ")
                    .append(session.getPudelWorld().getMusicManager().getTrackFormat(getListObject(session.getData("music.play.searching.top5",false),i)))
                    .append("](").append(session.getPudelWorld().getMusicManager().getTrackUrl(getListObject(session.getData("music.play.searching.top5",false),i)))
                    .append(")\n");
        }
        str.append("\n").append(getPudelWorld().getLocalizationManager().getLocalizedText(session, "music.manager.searching.tooltips",null));
        return str.toString();
    }

    public AudioTrack castAudioTrack(Object track){
        return (AudioTrack) track;
    }

    public String getTrackFormat(Object track) {
        return getTrackTitle(track) + " - " + getTrackUploader(track);
    }

    public String getTrackThumbnail(Object track){
        Matcher matcher = Pattern.compile("v=([^&]+)").matcher(castAudioTrack(track).getInfo().uri);
        return matcher.find() ? "https://img.youtube.com/vi/" + matcher.group(1) + "/maxresdefault.jpg" : "https://puu.sh/KgqvW.gif";
    }

    public String getTrackTitle(Object track){
        return castAudioTrack(track).getInfo().title;
    }

    public String getTrackUrl(Object track){
        return castAudioTrack(track).getInfo().uri;
    }

    public String getTrackUploader(Object track){
        return castAudioTrack(track).getInfo().author;
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public MusicManager initialize() {
        return this;
    }

    @Override
    public MusicManager reload() {
        return this;
    }

    @Override
    public MusicManager shutdown() {
        return this;
    }
}
