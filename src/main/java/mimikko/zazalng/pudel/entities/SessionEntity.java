package mimikko.zazalng.pudel.entities;

import mimikko.zazalng.pudel.contracts.SessionState;

import java.util.HashMap;
import java.util.Map;

public class SessionEntity {
    private final String userId;
    private final String guildId;
    private final Map<String, Object> data = new HashMap<>();
    private SessionState state;

    public SessionEntity(String userId, String guildId) {
        this.userId = userId;
        this.guildId = guildId;
        this.state = SessionState.INIT;
    }

    public void setState(SessionState state) {
        this.state = state;
    }

    public SessionState getState() {
        return state;
    }

    public void addData(String key, Object value) {
        data.put(key, value);
    }

    public Object getData(String key) {
        return data.get(key);
    }
}
