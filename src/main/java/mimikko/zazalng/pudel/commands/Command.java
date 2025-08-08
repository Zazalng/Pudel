package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.InteractionEntity;
import org.jetbrains.annotations.Nullable;

public interface Command{
    void execute(InteractionEntity session);
    void terminate(InteractionEntity session);
    String getDescription(InteractionEntity session);
    String getDetailedHelp(InteractionEntity session);
}