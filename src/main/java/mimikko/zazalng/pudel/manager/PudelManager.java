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
import net.dv8tion.jda.api.requests.RestAction;

public class PudelManager extends AbstractManager {
    private UserEntity PudelEntity;

    public PudelManager(PudelWorld pudelWorld){
        super(pudelWorld);
    }
    public String getName(SessionEntity session) {
        if(session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname()==null){
            return getLocalizationManager().getLocalizedText(session,"bot.name",null);
        } else{
            return session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname();
        }
    }

    public String getInviteURL(){
        return getPudelWorld().getEnvironment().getInviteURL();
    }

    public UserEntity getPudelEntity(){
        return this.PudelEntity;
    }

    public PudelManager setPudelEntity(UserEntity pudelEntity) {
        this.PudelEntity = pudelEntity;
        return this;
    }

    public PudelManager addReactions(Message msg, String... unicodes) {
        if (unicodes == null || unicodes.length == 0) return this;

        RestAction<Void> reactionChain = msg.addReaction(Emoji.fromFormatted(unicodes[0]));

        for (int i = 1; i < unicodes.length; i++) {
            String unicode = unicodes[i]; // Create a final copy for the lambda
            reactionChain = reactionChain.flatMap(ignored -> msg.addReaction(Emoji.fromFormatted(unicode)));
        }

        // Queue the chained reactions
        reactionChain.queue();

        return this;
    }

    public PudelManager openVoiceConnection(SessionEntity session, AudioSendHandler audio){
        openVoiceConnection(session.getGuild().getJDA(),session.getUser().getJDA(),audio);
        return this;
    }

    public PudelManager openVoiceConnection(InteractionEntity interaction, AudioSendHandler audio){
        openVoiceConnection(interaction.getMessage().getGuild(),interaction.getUser().getJDA(),audio);
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
