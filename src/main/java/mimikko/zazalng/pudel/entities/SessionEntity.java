package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.commands.Command;
import net.dv8tion.jda.api.entities.channel.Channel;

import java.util.HashMap;
import java.util.Map;

public class SessionEntity {
    private final UserEntity user;
    private final GuildEntity guild;
    private final Channel channelId;
    private final Map<String, Object> data = new HashMap<>();
    private String sessionState;

    public SessionEntity(UserEntity user, GuildEntity guild, Channel channelIssue, Command command) {
        this.user = user;
        this.guild = guild;
        this.channelId = channelIssue;
        this.sessionState = "INIT";
    }

    public void setState(String sessionState) {
        this.sessionState = sessionState;
    }

    public String getState() {
        return this.sessionState;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
