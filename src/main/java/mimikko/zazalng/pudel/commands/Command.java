package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import org.jetbrains.annotations.Nullable;

public interface Command{
    void execute(InteractionEntity session, @Nullable String args);
    void reload();
    void terminate(InteractionEntity session);
    String getDescription(InteractionEntity session);
    String getDetailedHelp(InteractionEntity session);
}