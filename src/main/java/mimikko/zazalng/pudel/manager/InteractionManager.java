package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.InteractionEntity;

import java.util.HashMap;
import java.util.Map;

public class InteractionManager implements Manager{
    protected final PudelWorld pudelWorld;
    private final Map<String,InteractionEntity> interactionList;

    public InteractionManager(PudelWorld pudelWorld) {
        this.pudelWorld = pudelWorld;
        this.interactionList = new HashMap<>();
    }

    /**
     * @return
     */
    @Override
    public PudelWorld getPudelWorld() {
        return null;
    }

    /**
     * @return
     */
    @Override
    public InteractionManager initialize() {
        return this;
    }

    /**
     * @return
     */
    @Override
    public InteractionManager reload() {
        return this;
    }

    /**
     *
     */
    @Override
    public void shutdown() {

    }
}
