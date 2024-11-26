package mimikko.zazalng.pudel.utility;

import java.util.List;

public class ListUtility {
    public static int getListSize(Object list){
        return ((List<?>) list).size();
    }

    public static Object getListObject(Object list, int index){
        return ((List<?>) list).get(index);
    }
}
