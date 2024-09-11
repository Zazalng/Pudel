package mimikko.zazalng.pudel.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;
import mimikko.zazalng.pudel.handlers.audiohandler.AudioResultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MusicManager implements Manager {
    private static final Logger logger = LoggerFactory.getLogger(CommandManager.class);
    protected final PudelWorld pudelWorld;
    private final AudioPlayerManager playerManager;

    public MusicManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager(
                true,   // Allow search
                true,   // Allow direct video IDs
                true/*,   // Allow direct playlist IDs
                new Client[]{new Music(), new Web()} */ // Clients
        );
        this.playerManager = new DefaultAudioPlayerManager();
        this.playerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerRemoteSources(this.playerManager,com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class);
    }

    public AudioPlayer musicManagerBuilder(){
        return this.playerManager.createPlayer();
    }

    public String loadAndPlay(MusicPlayerEntity player, String trackURL) {
        AudioResultHandler handler = new AudioResultHandler(player);
        playerManager.loadItem(trackURL, handler);

        if(handler.getResult().startsWith("URL")){
            trackURL = "Playlist had loaded successfully.\n`"+trackURL+"`";
        } else if(handler.getResult().startsWith("NULL")){
            trackURL = "No Matching result of input.\n`"+trackURL+"`";
        } else if(handler.getResult().startsWith("SEARCH")){
            trackURL = "Searching and Playing.\n"+ handler.getResult().replace("SEARCH","");
        } else{
            trackURL = handler.getResult();
        }

        logger.debug(trackURL);
        return trackURL;
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
