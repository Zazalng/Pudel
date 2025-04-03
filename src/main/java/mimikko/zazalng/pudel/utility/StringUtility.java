package mimikko.zazalng.pudel.utility;

public class StringUtility {
    public static boolean isEqualStr(String str, String arg){
        return str.equals(arg);
    }

    public static String stringFormat(String format, Object...args){
        return String.format(format, args);
    }

    public static String toString(Object toString){
        return String.valueOf(toString);
    }
}