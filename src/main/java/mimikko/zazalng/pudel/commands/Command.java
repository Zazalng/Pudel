package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public interface Command {
    void execute(GuildEntity guild, UserEntity user, String replyChannel, String args);
    void reload();
    String getDescription();
    String getDetailedHelp();

    void execute(SessionEntity session, String args);

    void continueExecution(SessionEntity session, String input);
}