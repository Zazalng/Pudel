package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.contracts.SessionState;
import mimikko.zazalng.pudel.manager.SessionManager;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Command{
    private final Map<String, SessionManager> sessions = new HashMap<>();

    // Start a new session or continue an existing one
    public void handleCommand(GuildEntity guild, UserEntity user, String channelId, String args, MessageReceivedEvent e) {
        String userId = user.getUser().getId();
        SessionManager session = sessions.computeIfAbsent(userId, k -> new SessionManager(userId, guild.getGuild().getId(), this));

        if (session.getState() == SessionState.INIT) {
            startSession(session, e);
        } else {
            continueSession(session, args, e);
        }
    }

    // Abstract methods for starting and continuing a session
    protected abstract void startSession(SessionManager session, MessageReceivedEvent e);
    protected abstract void continueSession(SessionManager session, String input, MessageReceivedEvent e);

    // Helper method to set session state
    protected void setSessionState(SessionManager session, SessionState state) {
        session.setState(state);
    }

    // Helper method to add data to the session
    protected void addSessionData(SessionManager session, String key, Object value) {
        session.addData(key, value);
    }

    // End the session
    protected void endSession(String userId) {
        sessions.remove(userId);
    }
}
