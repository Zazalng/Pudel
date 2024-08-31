package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.GuildEntity;
import mimikko.zazalng.pudel.entities.UserEntity;

public interface Command {
    void execute(GuildEntity guild, UserEntity user, String replyChannel, String args);
    String getDescription();
    String getDetailedHelp();
}