package mimikko.zazalng.pudel.commands.music;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.SessionEntity;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> localizationArgs = new HashMap<>();
        localizationArgs.put("args",args);
        args = args.startsWith("http://") || args.startsWith("https://") ? args : "ytsearch:" + args;

        session.getPudelWorld().getMusicManager().loadAndPlay(session, args, result -> {
            if(result.equals("error")){
                session.getChannel().sendMessage(localize(session,"music.play.init.error",localizationArgs)).queue();
            } else if(result.startsWith("playlist.")){
                localizationArgs.put("track.url",result.replace("playlist.",""));
                session.getChannel().sendMessage(localize(session, "music.play.init.playlist",localizationArgs)).queue();
                session.getGuild().getJDA().getAudioManager().setSendingHandler(session.getGuild().getMusicPlayer().getPlayer());
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
            } else if(result.startsWith("searching.")){
                session.getPudelWorld().getEmbedManager().createEmbed()
                        .setAuthor(session.getUser().getUserManager().getUserName(session),null,session.getUser().getJDA().getAvatarUrl())
                        .setThumbnail("https://puu.sh/KgdPy.gif")
                        .setFooter(this.getClass().getName())
                        .setTitle(String.format("%s: %s",localize(session, "music.play.init.searching"),result));
            } else{
                localizationArgs.put("track.info",result);
                session.getChannel().sendMessage(localize(session,"music.play.init",localizationArgs)).queue();
                session.getGuild().getJDA().getAudioManager().setSendingHandler(session.getGuild().getMusicPlayer().getPlayer());
                session.getPudelWorld().getPudelManager().OpenVoiceConnection(session);
            }
            session.setState("END");
        });
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
