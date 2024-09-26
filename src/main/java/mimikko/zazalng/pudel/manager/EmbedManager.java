package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedManager implements Manager{
    protected final PudelWorld pudelWorld;

    public EmbedManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
    }

    public EmbedBuilder getBuilder(){
        return new EmbedBuilder();
    }

    @Override
    public PudelWorld getPudelWorld() {
        return pudelWorld;
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
