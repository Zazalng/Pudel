package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.commands.CommandState;
import mimikko.zazalng.pudel.entities.MusicPlayerEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.List;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand{

    // Enum for MusicPlay states
    public enum state implements CommandState {
        SEARCHING;
    }

    // Entry point of the command execution
    @Override
    public MusicPlay execute(SessionEntity session, String args) {
        switch ((state) session.getState()) {
            case SEARCHING:
                handleSearchingState(session, args);
                break;
            default:
                initialState(session, args);
                break;
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

        super.stateEnd(session);
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

        super.stateEnd(session);
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
        session.getPudelWorld().getMusicManager().loadAndPlay(session, url).thenAccept(result ->{
            switch (result.getType()) {
                case SEARCH:
                    List topTracks = (List) session.addData("music.play.searching.top5",result.getTopTracks()).getData("music.play.searching.top5",false);
                    StringBuilder searchResults = new StringBuilder();
                    for (int i = 0; i < topTracks.size(); i++) {
                        searchResults.append(String.format("[%d. %s](%s)\n",i+1,session.getPudelWorld().getMusicManager().getTrackFormat(topTracks.get(i)),session.getPudelWorld().getMusicManager().getTrackUrl(topTracks.get(i))));
                    }
                    searchResults.append(localize(session,"music.play.searching.tooltips"));

                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(localize(session, "music.play.searching") + "\n" + result.getInput().substring(9))
                                    .setThumbnail("https://puu.sh/KgdPy.gif")
                                    .setDescription(searchResults.toString())
                                    .build()
                    ).queue();

                    session.setState(state.SEARCHING);
                    break;
                case TRACK:
                    session.getPudelWorld().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(session.getPudelWorld().getMusicManager().getTrackFormat(result.getTrack()), session.getPudelWorld().getMusicManager().getTrackUrl(result.getTrack()))
                                    .setThumbnail(session.getPudelWorld().getMusicManager().getTrackThumbnail(result.getTrack()))
                                    .setDescription(localize(session, "music.play.accept"))
                                    .build()
                    ).queue();
                    super.stateEnd(session);
                    break;
                case PLAYLIST:
                    session.getPudelWorld().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(result.getPlaylist().getName(), result.getInput())
                                    .setThumbnail("https://puu.sh/KgxX3.gif")
                                    .setDescription(localize(session, "music.play.playlist"))
                                    .build()
                    ).queue();
                    super.stateEnd(session);
                    break;
                default:
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().createEmbed(session)
                                    .setTitle(localize(session, "music.play.exception"))
                                    .setThumbnail("https://puu.sh/KgAxi.gif")
                                    .setDescription(result.getInput())
                                    .build()
                    ).queue();
                    super.stateEnd(session);
                    break;
            }
        });

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
                super.stateEnd(session);
            }
        } else {
            super.stateEnd(session);
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