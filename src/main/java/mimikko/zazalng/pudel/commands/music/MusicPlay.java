package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import static mimikko.zazalng.pudel.utility.BooleanUtility.betweenNumeric;
import static mimikko.zazalng.pudel.utility.BooleanUtility.isNumberic;
import static mimikko.zazalng.pudel.utility.ListUtility.*;

public class MusicPlay extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args) {
        super.execute(session, args);
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
        args = args.startsWith("http://") || args.startsWith("https://") ? args : "ytsearch:" + args;

        session.getPudelWorld().getMusicManager().loadAndPlay(session, args, result -> {
            if(result.equals("error")){
                session.getChannel().sendMessage(localize(session,"music.play.init.error",localizationArgs)).queue();
                session.setState("END");
            } else if(result.startsWith("playlist.")){
                localizationArgs.put("track.url",result.replace("playlist.",""));
                session.getChannel().sendMessage(localize(session, "music.play.init.playlist",localizationArgs)).queue();
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
                session.setState("END");
            } else if(result.startsWith("searching.")){
                session.addData("embed",session.getPudelWorld().getEmbedManager().createEmbed(session)
                        .setThumbnail("https://puu.sh/KgdPy.gif")
                        .setDescription(String.format("%s",localize(session, "music.play.init.searching.description")))
                        .setFooter(this.getClass().getName() + " | "+ session.getState())
                        .setTitle(String.format("%s: %s",localize(session, "music.play.init.searching"),result)));
                showlistEmbed(session).getPudelWorld();
                session.getPudelWorld().getPudelManager().sendingEmbed(session);
                session.setState("MUSIC.PLAY.SEARCHING");
            } else{
                localizationArgs.put("track.info",result);
                session.getChannel().sendMessage(localize(session, "music.play.init",localizationArgs)).queue();
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
                session.setState("END");
            }
        });
    }

    private SessionEntity queuingSong(SessionEntity session, String url){
        session.getPudelWorld().getMusicManager().loadAndPlay(session, url, result ->{
            session.getPudelWorld().getEmbedManager().createEmbed(session)
                    .setTitle(result,url)
                    .setDescription(localize(session, "music.play.queueUp"));
        });
        return session;
    }

    private SessionEntity searchingState(SessionEntity session, String args){
        if(isNumberic(args)){
            int index = Integer.parseInt(args);
            if(betweenNumeric(index,0,4,true)){
                queuingSong(session,session.getPudelWorld().getMusicManager().getTrackUrl(getListObject(session.getData("music.play.searching.top5",true),index)));
            } else{

            }
        }

        return session;
    }

    private SessionEntity showlistEmbed(SessionEntity session){
        for(int i = 0; i<getListSize(session.getData("music.play.searching.top5",false));i++){
            session.getPudelWorld().getEmbedManager().castEmbedBuilder(session, "embed")
                    .addField(session
                            .getPudelWorld()
                            .getMusicManager()
                            .getTrackFormat(getListObject(session.getData("music.play.searching.top5",false),i))
                                ,session.getPudelWorld().getMusicManager().getTrackUploader(getListObject(session.getData("music.play.searching.top5",false),i))
                                ,false);
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
