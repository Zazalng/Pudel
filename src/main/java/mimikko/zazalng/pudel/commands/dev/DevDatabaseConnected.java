package mimikko.zazalng.pudel.commands.dev;

import mimikko.zazalng.pudel.commands.AbstractCommand;
import mimikko.zazalng.pudel.entities.InteractionEntity;
import mimikko.zazalng.pudel.entities.SessionEntity;

public class DevDatabaseConnected extends AbstractCommand {
    @Override
    public void execute(SessionEntity session, String args){

    }

    @Override
    public void execute(InteractionEntity interaction){

    }
    /**
     * @param session
     * @return
     */
    @Override
    public String getDescription(SessionEntity session) {
        return "";
    }

    /**
     * @param session
     * @return
     */
    @Override
    public String getDetailedHelp(SessionEntity session) {
        return "";
    }
}
