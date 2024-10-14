package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand {
    @Override
    public MusicPlay execute(SessionEntity session, String args) {
        super.execute(session, args);
        if(session.getState().equals("MUSIC.PLAY.SEARCHING")){
            searchingState(session,args);
        }
        return this;
    }

    @Override
    public MusicPlay initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            if(session.getGuild().getMusicPlayer().getPlayingTrack()==null){
                session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(localize(session, "music.play.empty"))
                        .setThumbnail("https://puu.sh/KgLS9.gif")
                        .build()
                ).queue();
            } else {
                args = session.getPudelWorld().getMusicManager().getTrackFormat(session.getGuild().getMusicPlayer().getPlayingTrack());
                session.getChannel().sendMessageEmbeds(session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(args, session.getPudelWorld().getMusicManager().getTrackUrl(session.getGuild().getMusicPlayer().getPlayingTrack()))
                        .setThumbnail(session.getPudelWorld().getMusicManager().getTrackThumbnail(session.getGuild().getMusicPlayer().getPlayingTrack()))
                        .addField(localize(session, "music.play.loop"), session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isLoop()), true)
                        .addField(localize(session, "music.play.shuffle"), session.getPudelWorld().getLocalizationManager().getBooleanText(session, session.getGuild().getMusicPlayer().isShuffle()), true)
                        .addField(localize(session, "music.play.duration"), session.getPudelWorld().getMusicManager().getTrackDuration(session.getGuild().getMusicPlayer().getPlayingTrack()), true)
                        .build()
                ).queue();
            }
            session.setState("END");
            return this;
        }

        if (!session.getUser().getUserManager().isVoiceActive(session)) {
            args = localize(session,"music.play.error.voicechat");
            session.getChannel().sendMessage(args).queue();
            session.setState("END");
            return this;
        }
        localizationArgs.put("args",args);

        if (!args.startsWith("http://") && !args.startsWith("https://")) {
            args = "ytsearch:" + args;
        }
        queuingSong(session,args);
        return this;
    }

    private MusicPlay queuingSong(SessionEntity session, String url){
        session.getPudelWorld().getMusicManager().loadAndPlay(session, url);
        return this;
    }

    private MusicPlay searchingState(SessionEntity session, String args){
        if(isNumberic(args)){
            int index = Integer.parseInt(args) - 1;
            try{
                queuingSong(session,session.getPudelWorld().getMusicManager().getTrackUrl(getListObject(session.getData("music.play.searching.top5",true),index)));
            }catch(IndexOutOfBoundsException e){
                session.setState("END");
            }
        } else{
            session.setState("END");
        }
        return this;
    }

    @Override
    public MusicPlay reload() {
        return this;
    }

    @Override
    public String getDescription(SessionEntity session) {
        return localize(session,"music.play.help");
    }

    @Override
    public String getDetailedHelp(SessionEntity session) {
        return localize(session,"music.play.details");
    }
}
