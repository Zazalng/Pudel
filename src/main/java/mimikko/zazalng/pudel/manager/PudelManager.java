package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;

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

    public PudelManager addRection(Message msg, String unicode){
        msg.addReaction(Emoji.fromFormatted(unicode)).queue();
        return this;
    }

    public PudelManager openVoiceConnection(SessionEntity session, AudioSendHandler audio){
        session.getGuild().getJDA().getAudioManager().openAudioConnection(session.getGuild().getJDA().getMember(session.getUser().getJDA()).getVoiceState().getChannel());
        session.getGuild().getJDA().getAudioManager().setSendingHandler(audio);
        return this;
    }

    public PudelManager closeVoiceConnection(SessionEntity session){
        session.getGuild().getJDA().getAudioManager().closeAudioConnection();
        session.getGuild().getJDA().getAudioManager().setSendingHandler(null);
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
    public void shutdown() {

    }
}
