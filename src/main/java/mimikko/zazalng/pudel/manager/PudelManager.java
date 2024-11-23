package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;

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
        openVoiceConnection(session.getGuild().getJDA(),session.getUser().getJDA(),audio);
        return this;
    }

    public PudelManager openVoiceConnection(InteractionEntity interaction, AudioSendHandler audio){
        openVoiceConnection(interaction.getMessage().getGuild(),interaction.getInteractor().getJDA(),audio);
        return this;
    }

    private PudelManager openVoiceConnection(Guild g, User u, AudioSendHandler audio){
        g.getAudioManager().openAudioConnection(g.getMember(u).getVoiceState().getChannel());
        g.getAudioManager().setSendingHandler(audio);
        return this;
    }

    public PudelManager closeVoiceConnection(SessionEntity session){
        closeVoiceConnection(session.getGuild().getJDA());
        return this;
    }

    public PudelManager closeVoiceConnection(InteractionEntity interaction){
        closeVoiceConnection(interaction.getMessage().getGuild());
        return this;
    }

    private PudelManager closeVoiceConnection(Guild g){
        g.getAudioManager().closeAudioConnection();
        g.getAudioManager().setSendingHandler(null);
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
    public void reload() {

    }

    @Override
    public void shutdown() {

    }
}
