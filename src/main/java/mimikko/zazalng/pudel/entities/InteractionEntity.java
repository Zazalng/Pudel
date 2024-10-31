package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.commands.Command;
import mimikko.zazalng.pudel.manager.InteractionManager;

public class InteractionEntity {
    protected final InteractionManager interactionManager;
    private final String messageId;
    private final UserEntity interactor;
    private final Command issueCommand;
}
