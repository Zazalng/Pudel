package mimikko.zazalng.pudel.commands;

import mimikko.zazalng.pudel.entities.SessionEntity;

public abstract class AbstractCommand implements Command {
    @Override
    public void execute(SessionEntity session, String args) {
        // Default implementation for starting a session
        session.addData("args", args);  // Save initial data
        // Custom logic for starting the command process
    }

    @Override
    public void continueExecution(SessionEntity session, String input) {
        // Default implementation for continuing a session
        // Custom logic for handling user input in multi-step commands
    }
}