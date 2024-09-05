package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class PudelManager implements Manager {
    protected PudelWorld pudelWorld;
    private String pudelId;

    public PudelManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    public void setPudelID(String pudelId){
        this.pudelId = pudelId;
    }

    public String getPudelId(){
        return this.pudelId;
    }

    public void OpenVoiceConnection(Guild guild, VoiceChannel channel){
        guild.getAudioManager().openAudioConnection(channel);
    }

    public void CloseVoiceConnection(Guild guild){
        guild.getAudioManager().closeAudioConnection();
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
