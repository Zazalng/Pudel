package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.*;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
        if(session.getState().equals("MUSIC.PLAY.SEARCHING")){
            searchingState(session,args);
        }
    }

    @Override
    protected void initialState(SessionEntity session, String args) {
        if (args.isEmpty()) {
            args = localize(session,"music.play.error.input");
            session.getChannel().sendMessage(args).queue();
            session.setState("END");
            return;
        }

        if (!session.getUser().getUserManager().isVoiceActive(session)) {
            args = localize(session,"music.play.error.voicechat");
            session.getChannel().sendMessage(args).queue();
            session.setState("END");
            return;
        }
        localizationArgs.put("args",args);

        if(args.startsWith("http://") || args.startsWith("https://")){
            queuingSong(session,args);
        } else{
            args = "ytsearch:"+args;
            queuingSong(session, args);
        }
    }

    private SessionEntity queuingSong(SessionEntity session, String url){
        session.getPudelWorld().getMusicManager().loadAndPlay(session, url, result ->{
            if(result.startsWith("accept:")){
                session.addData("music.play.accepted",session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setTitle(result,url)
                        .setThumbnail(result.split(":",2)[1])
                        .setDescription(localize(session, "music.play.queueUp")));
                session.getPudelWorld().getPudelManager().sendingEmbed(session,"music.play.accepted");
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
                session.setState("END");
            } else{
                if(result.startsWith("error:")){
                    session.getChannel().sendMessage(localize(session,"music.play.init.error",localizationArgs)).queue();
                    session.setState("END");
                } else if(result.startsWith("playlist:")){
                    localizationArgs.put("track.url",result.split(":",2)[1]);
                    session.getChannel().sendMessage(localize(session, "music.play.init.playlist",localizationArgs)).queue();
                    session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
                    session.setState("END");
                } else if(result.startsWith("ytsearch:")){
                    session.addData("music.play.searching",session.getPudelWorld().getEmbedManager().createEmbed(session)
                            .setDescription(localize(session, "music.play.init.searching.description"))
                            .setThumbnail("https://puu.sh/KgdPy.gif")
                            .setTitle(String.format("%s: %s",localize(session, "music.play.init.searching"),result.split(":",2)[1])));
                    showlistEmbed(session);
                    session.getPudelWorld().getPudelManager().sendingEmbed(session,"music.play.searching");
                    session.setState("MUSIC.PLAY.SEARCHING");
                }
            }
        });
        return session;
    }

    private SessionEntity searchingState(SessionEntity session, String args){
        if(isNumberic(args)){
            int index = Integer.parseInt(args)-1;
            if(betweenNumeric(index,0,4,true)){
                queuingSong(session,session.getPudelWorld().getMusicManager().getTrackUrl(getListObject(session.getData("music.play.searching.top5",true),index)));
            } else{

            }
        }

        return session;
    }

    private SessionEntity showlistEmbed(SessionEntity session){
        for(int i = 0; i<getListSize(session.getData("music.play.searching.top5",false));i++){
            session.getPudelWorld().getEmbedManager().castEmbedBuilder(session, "music.play.searching")
                    .addField(session
                            .getPudelWorld()
                            .getMusicManager()
                            .getTrackTitle(getListObject(session.getData("music.play.searching.top5",false),i))
                            ,session
                            .getPudelWorld()
                            .getMusicManager()
                            .getTrackUploader(getListObject(session.getData("music.play.searching.top5",false),i)),false);
        }
        return session;
    }

    @Override
    public void reload() {

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
