package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class PudelManager implements Manager {
    protected PudelWorld pudelWorld;
    private UserEntity PudelEntity;

    public PudelManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }
    public String getName(GuildEntity guild) {
        if(guild.getJDA().getMemberById(getJDA().getId()).getNickname()==null){
            return this.pudelWorld.getLocalizationManager().getLocalizedText("bot.name",guild.getLanguageCode(),null);
        } else{
            return guild.getJDA().getMemberById(getJDA().getId()).getNickname();
        }
    }

    public String getPudelId(){
        return PudelEntity.getJDA().getId();
    }

    public User getJDA() {
        return PudelEntity.getJDA();
    }

    public void setPudelEntity(UserEntity pudelEntity) {
        PudelEntity = pudelEntity;
    }

    public void sendingMessage(){

    }

    public void OpenVoiceConnection(GuildEntity guild, VoiceChannel channel){
        guild.getJDA().getAudioManager().openAudioConnection(channel);
    }

    public void CloseVoiceConnection(GuildEntity guild){
        guild.getJDA().getAudioManager().setSendingHandler(null);
        guild.getJDA().getAudioManager().closeAudioConnection();
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
