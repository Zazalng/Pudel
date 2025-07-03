package mimikko.zazalng.pudel.manager;

import mimikko.zazalng.pudel.PudelWorld;
import mimikko.zazalng.pudel.entities.UserEntity;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager extends AbstractManager{
    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    //private final String [] connection;

    protected DatabaseManager(PudelWorld pudelWorld){
        super(pudelWorld);
    }

    @Override
    public boolean initialize(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean reload(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }

    @Override
    public boolean shutdown(User user){
        if(!super.isAuthorized(user)){
            logger.error("400 Bad Request");
            return false;
        }

        return true;
    }
}
