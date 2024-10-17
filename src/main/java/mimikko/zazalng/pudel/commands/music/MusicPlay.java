package mimikko.zazalng.pudel.commands.music;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.contracts.Command.BaseCommandState;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.handlers.MusicResultHandler;

import java.util.List;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand<MusicPlay.State> {

    // Enum for MusicPlay states
    public enum State implements BaseCommandState {
        SEARCHING;

        @Override
        public String getName() {
            return name();
        }
    }

    // Entry point of the command execution
    @Override
    public MusicPlay execute(SessionEntity session, String args) {
        super.execute(session, args);

        // Handle specific state: SEARCHING
        if (session.getState().equals(State.SEARCHING.getName())) {
            return handleSearchingState(session, args);
        }

        return this;
    }

    // Initialization logic when state is INIT
    @Override
    public MusicPlay initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            handleNoArgs(session);
        } else if (!isUserInVoiceChat(session)) {
            sendNotInVoiceChat(session);
        } else {
            // Proceed to queue the song based on input
            queueSong(session, args);
        }
        return this;
    }

    // Reload command logic
    @Override
    public MusicPlay reload() {
        return this;
    }

    // Description and detailed help methods
    @Override
    public String getDescription(SessionEntity session) {
        return localize(session, "music.play.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session, "music.play.details");
    }

    // ---- Private Methods ----

    // Handle case when no arguments are provided
    private MusicPlay handleNoArgs(SessionEntity session) {
        MusicPlayerEntity player = session.getGuild().getMusicPlayer();

        if (player.getPlayingTrack() == null) {
            sendNoTrackMessage(session);
        } else {
            sendCurrentTrackMessage(session, player);
        }

        session.setState(BaseCommandState.END);

        return this;
    }

    // Check if user is in a voice channel
    private boolean isUserInVoiceChat(SessionEntity session) {
        return session.getUser().getUserManager().isVoiceActive(session);
    }

    // Handle case when user is not in a voice channel
    private MusicPlay sendNotInVoiceChat(SessionEntity session) {
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(localize(session, "music.play.error.voicechat"))
                        .setThumbnail("https://puu.sh/KgP67.gif")
                        .build()
        ).queue();

        session.setState(BaseCommandState.END);
        return this;
    }

    // Queue the song or search
    private MusicPlay queueSong(SessionEntity session, String args) {
        if (!args.startsWith("http://") && !args.startsWith("https://")) {
            args = "ytsearch:" + args;
        }
        queueTrack(session, args);

        return this;
    }

    // Queue a song URL
    private MusicPlay queueTrack(SessionEntity session, String url) {
        MusicResultHandler result = session.getPudelWorld().getMusicManager().loadAndPlay(session, url);

        // If it's a search result, display the top 5 tracks to the user
        if (result.getType() == MusicResultHandler.Type.SEARCH) {
            StringBuilder searchResults = new StringBuilder("Top 5 Search Results:\n");
            List<AudioTrack> topTracks = result.getTopTracks();
            for (int i = 0; i < topTracks.size(); i++) {
                searchResults.append(i + 1).append(". ").append(topTracks.get(i).getInfo().title).append("\n");
            }
            session.getChannel().sendMessage(searchResults.toString()).queue();

            // Set the session to SEARCHING state
            session.setState(State.SEARCHING.getName());
        }

        return this;
    }

    // Handle searching state
    private MusicPlay handleSearchingState(SessionEntity session, String args) {
        if (isNumeric(args)) {
            int index = Integer.parseInt(args) - 1;
            try {
                String trackUrl = session.getPudelWorld().getMusicManager().getTrackUrl(getListObject(session.getData("music.play.searching.top5", true), index));
                queueTrack(session, trackUrl);
            } catch (IndexOutOfBoundsException e) {
                session.setState(BaseCommandState.END);
            }
        } else {
            session.setState(BaseCommandState.END);
        }

        return this;
    }

    // Send no track currently playing message
    private MusicPlay sendNoTrackMessage(SessionEntity session) {
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(localize(session, "music.play.empty"))
                        .setThumbnail("https://puu.sh/KgLS9.gif")
                        .build()
        ).queue();

        return this;
    }

    // Send current track details
    private MusicPlay sendCurrentTrackMessage(SessionEntity session, MusicPlayerEntity player) {
        String trackFormat = session.getPudelWorld().getMusicManager().getTrackFormat(player.getPlayingTrack());
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(trackFormat, session.getPudelWorld().getMusicManager().getTrackUrl(player.getPlayingTrack()))
                        .setThumbnail(session.getPudelWorld().getMusicManager().getTrackThumbnail(player.getPlayingTrack()))
                        .addField(localize(session, "music.play.loop"),
                                session.getPudelWorld().getLocalizationManager().getBooleanText(session, player.isLoop()), true)
                        .addField(localize(session, "music.play.shuffle"),
                                session.getPudelWorld().getLocalizationManager().getBooleanText(session, player.isShuffle()), true)
                        .addField(localize(session, "music.play.queueby"),
                                session.getPudelWorld().getUserManager().castUserEntity(player.getPlayingTrack().getUserData()).getJDA().getAsMention(), true)
                        .addField(localize(session, "music.play.duration"),
                                session.getPudelWorld().getMusicManager().getTrackDuration(player.getPlayingTrack()), false)
                        .build()
        ).queue();

        return this;
    }
}