package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.interaction.ReactionEntity;
import mimikko.zazalng.pudel.entities.interaction.TextEntity;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.requests.RestAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PudelManager extends AbstractManager {
    private static final Logger logger = LoggerFactory.getLogger(MusicManager.class);
    private User PudelEntity;

    protected PudelManager(PudelWorld pudelWorld){
        super(pudelWorld);
    }

    public String getName(TextEntity session) {
        if(session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname()==null){
            return getLocalizationManager().getLocalizedText(session,"bot.name",null);
        } else{
            return session.getGuild().getJDA().getMemberById(getPudelEntity().getJDA().getId()).getNickname();
        }
    }

    public String getInviteURL(){
        return getPudelWorld().getEnvironment().getInviteURL();
    }

    protected User getPudelEntity(){
        return this.PudelEntity;
    }

    public PudelManager setPudelEntity(User pudelEntity) {
        if(getPudelEntity() != null) {
            logger.error("Attempt to reassign Pudel's entity");
            return this;
        }

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

    public PudelManager openVoiceConnection(TextEntity session, AudioSendHandler audio){
        openVoiceConnection(session.getGuild().getJDA(),session.getUser().getJDA(),audio);
        return this;
    }

    public PudelManager openVoiceConnection(ReactionEntity interaction, AudioSendHandler audio){
        openVoiceConnection(interaction.getMessage().getGuild(),interaction.getUser().getJDA(),audio);
        return this;
    }

    private PudelManager openVoiceConnection(Guild g, User u, AudioSendHandler audio){
        g.getAudioManager().openAudioConnection(g.getMember(u).getVoiceState().getChannel());
        g.getAudioManager().setSendingHandler(audio);
        return this;
    }

    public PudelManager closeVoiceConnection(TextEntity session){
        closeVoiceConnection(session.getGuild().getJDA());
        return this;
    }

    public PudelManager closeVoiceConnection(ReactionEntity interaction){
        closeVoiceConnection(interaction.getMessage().getGuild());
        return this;
    }

    private PudelManager closeVoiceConnection(Guild g){
        g.getAudioManager().closeAudioConnection();
        g.getAudioManager().setSendingHandler(null);
        return this;
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}
