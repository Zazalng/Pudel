package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

public abstract class AbstractCommand implements Command {
    @Override
    public void execute(SessionEntity session, String args) {
        if(session.getState().equals("INIT")){
            initialState(session,args);
        }
    }
    protected void initialState(SessionEntity session, String args){}
}
