package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.List;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand{
    private state state;
    private enum state {
        SEARCHING
    }

    // Entry point of the command execution
    @Override
    public MusicPlay execute(SessionEntity session, String args) {
        switch (getState()) {
            case SEARCHING:
                handleSearchingState(session, args);
                break;
            case null:
                initialState(session, args);
                break;
        }
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
    private MusicPlay initialState(SessionEntity session, String args) {
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

    // Handle case when no arguments are provided
    private MusicPlay handleNoArgs(SessionEntity session) {
        if (session.getPudelWorld().getMusicManager().getPlayingTrack(session) == null) {
            sendNoTrackMessage(session);
        } else {
            sendCurrentTrackMessage(session);
        }

        super.terminate(session);
        return this;
    }

    // Check if user is in a voice channel
    private boolean isUserInVoiceChat(SessionEntity session) {
        return session.getUser().getUserManager().isVoiceActive(session);
    }

    // Handle case when user is not in a voice channel
    private MusicPlay sendNotInVoiceChat(SessionEntity session) {
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().embedCommand(session)
                        .setTitle(localize(session, "music.play.error.voicechat"))
                        .setThumbnail("https://puu.sh/KgP67.gif")
                        .build()
        ).queue();

        super.terminate(session);
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
                            session.getPudelWorld().getEmbedManager().embedCommand(session)
                                    .setTitle(localize(session, "music.play.searching") + "\n" + result.getInput().substring(9))
                                    .setThumbnail("https://puu.sh/KgdPy.gif")
                                    .setDescription(searchResults.toString())
                                    .build()
                    ).queue();

                    setState(state.SEARCHING);
                    break;
                case TRACK:
                    session.getPudelWorld().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().embedCommand(session)
                                    .setTitle(session.getPudelWorld().getMusicManager().getTrackFormat(result.getTrack()), session.getPudelWorld().getMusicManager().getTrackUrl(result.getTrack()))
                                    .setThumbnail(session.getPudelWorld().getMusicManager().getTrackThumbnail(result.getTrack()))
                                    .setDescription(localize(session, "music.play.accept"))
                                    .build()
                    ).queue();
                    super.terminate(session);
                    break;
                case PLAYLIST:
                    session.getPudelWorld().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().embedCommand(session)
                                    .setTitle(result.getPlaylist().getName(), result.getInput())
                                    .setThumbnail("https://puu.sh/KgxX3.gif")
                                    .setDescription(localize(session, "music.play.playlist"))
                                    .build()
                    ).queue();
                    super.terminate(session);
                    break;
                default:
                    session.getChannel().sendMessageEmbeds(
                            session.getPudelWorld().getEmbedManager().embedCommand(session)
                                    .setTitle(localize(session, "music.play.exception"))
                                    .setThumbnail("https://puu.sh/KgAxi.gif")
                                    .setDescription(result.getInput())
                                    .build()
                    ).queue();
                    super.terminate(session);
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
                super.terminate(session);
            }
        } else {
            super.terminate(session);
        }

        return this;
    }

    // Send no track currently playing message
    private MusicPlay sendNoTrackMessage(SessionEntity session) {
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().embedCommand(session)
                        .setTitle(localize(session, "music.play.empty"))
                        .setThumbnail("https://puu.sh/KgLS9.gif")
                        .build()
        ).queue();

        return this;
    }

    // Send current track details
    private MusicPlay sendCurrentTrackMessage(SessionEntity session) {
        String trackFormat = session.getPudelWorld().getMusicManager().getTrackFormat(session.getPudelWorld().getMusicManager().getPlayingTrack(session));
        session.getChannel().sendMessageEmbeds(
                session.getPudelWorld().getEmbedManager().embedCommand(session)
                        .setTitle(trackFormat, session.getPudelWorld().getMusicManager().getTrackUrl(session.getPudelWorld().getMusicManager().getPlayingTrack(session)))
                        .setThumbnail(session.getPudelWorld().getMusicManager().getTrackThumbnail(session.getPudelWorld().getMusicManager().getPlayingTrack(session)))
                        .addField(localize(session, "music.play.loop"),
                                localize(session, session.getPudelWorld().getMusicManager().getMusicPlayer(session).isLoop()), true)
                        .addField(localize(session, "music.play.shuffle"),
                                localize(session, session.getPudelWorld().getMusicManager().getMusicPlayer(session).isShuffle()), true)
                        .addField(localize(session, "music.play.remaining"),
                                String.valueOf(session.getPudelWorld().getMusicManager().getMusicPlayer(session).getActivePlaylist().size()),true)
                        .addField(localize(session, "music.play.duration"),
                                session.getPudelWorld().getMusicManager().getTrackDuration(session.getPudelWorld().getMusicManager().getPlayingTrack(session)), true)
                        .addField(localize(session, "music.play.queueby"),
                                session.getPudelWorld().getUserManager().castUserEntity(session.getPudelWorld().getMusicManager().getPlayingTrack(session).getUserData()).getJDA().getAsMention(), true)
                        .build()
        ).queue();

        return this;
    }

    private state getState(){
        return this.state;
    }

    private MusicPlay setState(state state){
        this.state = state;
        return this;
    }
}