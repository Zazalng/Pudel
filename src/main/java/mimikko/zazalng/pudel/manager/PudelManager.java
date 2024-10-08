package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.User;

public class PudelManager implements Manager {
    protected PudelWorld pudelWorld;
    private UserEntity PudelEntity;

    public PudelManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }
    public String getName(SessionEntity session) {
        if(session.getGuild().getJDA().getMemberById(getJDA().getId()).getNickname()==null){
            return this.pudelWorld.getLocalizationManager().getLocalizedText("bot.name",session.getGuild().getLanguageCode(),null);
        } else{
            return session.getGuild().getJDA().getMemberById(getJDA().getId()).getNickname();
        }
    }

    public User getJDA() {
        return PudelEntity.getJDA();
    }

    public PudelManager setPudelEntity(UserEntity pudelEntity) {
        PudelEntity = pudelEntity;
        return this;
    }

    public PudelManager sendingMessage(SessionEntity session){
        session.getChannel().sendMessage((String) session.getData("message",false)).queue();
        return this;
    }

    public PudelManager sendingEmbed(SessionEntity session, String key){
        session.getChannel().sendMessageEmbeds(getPudelWorld().getEmbedManager().castEmbedBuilder(session,key).build()).queue();
        return this;
    }

    public PudelManager OpenVoiceConnection(SessionEntity session){
        session.getGuild().getJDA().getAudioManager().openAudioConnection(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getVoiceState().getChannel());
        setSendingHandler(session);
        return this;
    }

    public PudelManager CloseVoiceConnection(SessionEntity session){
        session.getGuild().getJDA().getAudioManager().closeAudioConnection();
        setSendingHandler(session);
        return this;
    }

    public PudelManager setSendingHandler(SessionEntity session){
        session.getGuild().getJDA().getAudioManager().setSendingHandler(session.getGuild().getMusicPlayer().getPlayer());
        return this;
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
