package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.List;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;

public class MusicPlay extends AbstractCommand{
    private List topTracks;
    private state state;
    private enum state {
        SEARCHING,
        PLAYER
    }

    // Entry point of the command execution
    @Override
    public void execute(SessionEntity session, String args) {
        switch (getState()) {
            case SEARCHING:
                handleSearchingState(session, args);
                break;
            case null:
                initialState(session, args);
                break;
            case PLAYER:
                super.terminate(session);
                break;
        }
    }
    @Override
    public void execute(InteractionEntity interaction) {
        switch (getState()) {
            case SEARCHING:
                handleSearchingState(interaction);
                break;
            case PLAYER:
                handlePlayerState(interaction);
                break;
            case null:
                break;
        }
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
        if (session.getManager().getMusicManager().getPlayingTrack(session) == null) {
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
                session.getManager().getEmbedManager().embedCommand(session)
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
        session.getManager().getMusicManager().loadAndPlay(session, url).thenAccept(result ->{
            switch (result.getType()) {
                case SEARCH:
                    topTracks = result.getTopTracks();
                    StringBuilder searchResults = new StringBuilder();
                    for (int i = 0; i < topTracks.size(); i++) {
                        searchResults.append(String.format("[%d. %s](%s)\n",i+1,session.getManager().getMusicManager().getTrackFormat(topTracks.get(i)),session.getManager().getMusicManager().getTrackUrl(topTracks.get(i))));
                    }
                    searchResults.append(localize(session,"music.play.searching.tooltips"));

                    session.getChannel().sendMessageEmbeds(
                            session.getManager().getEmbedManager().embedCommand(session)
                                    .setTitle(localize(session, "music.play.searching") + "\n" + result.getInput().substring(9))
                                    .setThumbnail("https://puu.sh/KgdPy.gif")
                                    .setDescription(searchResults.toString())
                                    .build()
                    ).queue(e -> session.getManager().getInteractionManager().newInteraction(e,session, 15).getManager().getPudelManager()
                            .addReactions(e,
                                    "U+31U+fe0fU+20e3",//1
                                    "U+32U+fe0fU+20e3",//2
                                    "U+33U+fe0fU+20e3",//3
                                    "U+34U+fe0fU+20e3",//4
                                    "U+35U+fe0fU+20e3",//5
                                    "U+1F528")//hammer
                    );

                    setState(state.SEARCHING);
                    super.terminate(session);
                    break;
                case TRACK:
                    session.getManager().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getManager().getEmbedManager().embedCommand(session)
                                    .setTitle(session.getManager().getMusicManager().getTrackFormat(result.getTrack()), session.getManager().getMusicManager().getTrackUrl(result.getTrack()))
                                    .setThumbnail(session.getManager().getMusicManager().getTrackThumbnail(result.getTrack()))
                                    .setDescription(localize(session, "music.play.accept"))
                                    .build()
                    ).queue();
                    super.terminate(session);
                    break;
                case PLAYLIST:
                    session.getManager().getMusicManager().loadAndPlay(session, result);
                    session.getChannel().sendMessageEmbeds(
                            session.getManager().getEmbedManager().embedCommand(session)
                                    .setTitle(result.getPlaylist().getName(), result.getInput())
                                    .setThumbnail("https://puu.sh/KgxX3.gif")
                                    .setDescription(localize(session, "music.play.playlist"))
                                    .build()
                    ).queue();
                    super.terminate(session);
                    break;
                default:
                    session.getChannel().sendMessageEmbeds(
                            session.getManager().getEmbedManager().embedCommand(session)
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

    private MusicPlay queueTrack(InteractionEntity interaction, int index){
        interaction.getManager().getMusicManager().loadAndPlay(interaction, topTracks.get(index));
        return this;
    }

    // Handle searching state
    private MusicPlay handleSearchingState(SessionEntity session, String args) {
        if (isNumeric(args)) {
            int index = Integer.parseInt(args) - 1;
            try {
                queueTrack(session, session.getManager().getMusicManager().getTrackUrl(topTracks.get(index)));
            } catch (IndexOutOfBoundsException e) {
                super.terminate(session);
            }
        } else {
            super.terminate(session);
        }
        return this;
    }

    private MusicPlay handleSearchingState(InteractionEntity interaction) {
        String react = interaction.getReact();
        if (react.startsWith("U+3") && react.endsWith("U+20e3")) {
            int index = Character.getNumericValue(react.charAt(3)) - 1;
            queueTrack(interaction, index);
        } else{
            super.terminate(interaction);
        }
        return this;
    }

    private MusicPlay handlePlayerState(InteractionEntity interaction){
        switch(interaction.getReact()){
            case "U+23f9"://Stop
                interaction.getManager().getCommandManager().getCommand("stop").execute(interaction);
                interaction.getMessage().removeReaction(interaction.getReactAction(),interaction.getUser().getJDA()).queue();
                super.terminate(interaction);
                break;
            case "U+23ed"://next
                interaction.getManager().getCommandManager().getCommand("skip").execute(interaction);
                interaction.getMessage().removeReaction(interaction.getReactAction(),interaction.getUser().getJDA()).queue();
                break;
            case "U+1f501"://loop
                interaction.getManager().getCommandManager().getCommand("loop").execute(interaction);
                interaction.getMessage().removeReaction(interaction.getReactAction(),interaction.getUser().getJDA()).queue();
                break;
            case "U+1f500"://shuffle
                interaction.getManager().getCommandManager().getCommand("shuffle").execute(interaction);
                interaction.getMessage().removeReaction(interaction.getReactAction(),interaction.getUser().getJDA()).queue();
                break;
            case "U+2764U+fe0f"://Favorite
                //Implementation for Database
                break;
            default:
                break;
        }
        return this;
    }

    // Send no track currently playing message
    private MusicPlay sendNoTrackMessage(SessionEntity session) {
        session.getChannel().sendMessageEmbeds(
                session.getManager().getEmbedManager().embedCommand(session)
                        .setTitle(localize(session, "music.play.empty"))
                        .setThumbnail("https://puu.sh/KgLS9.gif")
                        .build()
        ).queue();

        return this;
    }

    // Send current track details
    private MusicPlay sendCurrentTrackMessage(SessionEntity session) {
        setState(state.PLAYER);
        String trackFormat = session.getManager().getMusicManager().getTrackFormat(session.getManager().getMusicManager().getPlayingTrack(session));
        session.getChannel().sendMessageEmbeds(
                session.getManager().getEmbedManager().embedCommand(session)
                        .setTitle(trackFormat, session.getManager().getMusicManager().getTrackUrl(session.getManager().getMusicManager().getPlayingTrack(session)))
                        .setThumbnail(session.getManager().getMusicManager().getTrackThumbnail(session.getManager().getMusicManager().getPlayingTrack(session)))
                        .addField(localize(session, "music.play.loop"),
                                localize(session, session.getManager().getMusicManager().getLoopKey(session)), true)
                        .addField(localize(session, "music.play.shuffle"),
                                localize(session, session.getManager().getMusicManager().getMusicPlayer(session).isShuffle()), true)
                        .addField(localize(session, "music.play.remaining"),
                                String.valueOf(session.getManager().getMusicManager().getMusicPlayer(session).getActivePlaylist().size()),true)
                        .addField(localize(session, "music.play.duration"),
                                session.getManager().getMusicManager().getTrackDuration(session.getManager().getMusicManager().getPlayingTrack(session)), true)
                        .addField(localize(session, "music.play.queueby"),
                                session.getManager().getUserManager().castUserEntity(session.getManager().getMusicManager().getPlayingTrack(session).getUserData()).getJDA().getAsMention(), true)
                        .build()
        ).queue(e -> session.getManager().getInteractionManager().newInteraction(e,session, false).getManager().getPudelManager()
                .addReactions(e,"U+23f9","U+23ed","U+1f501","U+1f500","U+2764U+fe0f"));//STOP,SKIP,LOOP,SHUFFLE,Favorite
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