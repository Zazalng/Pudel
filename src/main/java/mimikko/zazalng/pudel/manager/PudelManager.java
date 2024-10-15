package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public class PudelManager implements Manager {
    protected PudelWorld pudelWorld;
    private UserEntity PudelEntity;

    public PudelManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }
    public String getName(SessionEntity session) {
        if(session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname()==null){
            return this.pudelWorld.getLocalizationManager().getLocalizedText(session,"bot.name",null);
        } else{
            return session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname();
        }
    }

    public UserEntity getPudelEntity(){
        return this.PudelEntity;
    }

    public PudelManager setPudelEntity(UserEntity pudelEntity) {
        this.PudelEntity = pudelEntity;
        return this;
    }

    public PudelManager openVoiceConnection(SessionEntity session){
        session.getGuild().getJDA().getAudioManager().openAudioConnection(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getVoiceState().getChannel());
        setSendingHandler(session);
        return this;
    }

    public PudelManager closeVoiceConnection(SessionEntity session){
        closeVoiceConnection(session.getGuild());
        return this;
    }

    public PudelManager closeVoiceConnection(GuildEntity guild){
        guild.getJDA().getAudioManager().closeAudioConnection();
        setSendingHandler(guild);
        return this;
    }

    public PudelManager setSendingHandler(SessionEntity session){
        setSendingHandler(session.getGuild());
        return this;
    }

    public PudelManager setSendingHandler(GuildEntity guild){
        guild.getJDA().getAudioManager().setSendingHandler(guild.getMusicPlayer().getPlayer());
        return this;
    }

    @Override
    public PudelWorld getPudelWorld() {
        return this.pudelWorld;
    }

    @Override
    public PudelManager initialize() {
        return this;
    }

    @Override
    public PudelManager reload() {
        return this;
    }

    @Override
    public PudelManager shutdown() {
        getPudelWorld().getGuildManager().getGuildEntity().forEach(guild -> closeVoiceConnection(guild));
        return this;
    }
}
