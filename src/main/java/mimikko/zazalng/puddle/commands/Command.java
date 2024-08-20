package mimikko.zazalng.puddle.commands;

import mimikko.zazalng.puddle.entities.GuildEntity;
import mimikko.zazalng.puddle.entities.UserEntity;

public interface Command {
    void execute(GuildEntity guild, UserEntity user, String replyChannel, String args);
}