package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.EmbedEntity;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashMap;
import java.util.Map;

public class EmbedManager implements Manager{
    protected final PudelWorld pudelWorld;
    private final EmbedBuilder builder;
    private final Map<String, EmbedEntity> embedEntity;

    public EmbedManager(PudelWorld pudelWorld){
        this.pudelWorld = pudelWorld;
        this.builder = new EmbedBuilder();
        this.embedEntity = new HashMap<>();
    }

    public EmbedBuilder getBuilder(){
        return this.builder;
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
