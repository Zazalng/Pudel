package mimikko.zazalng.pudel.utility;

public class BooleanUtility {
    public static boolean triggerTrue(String tryLogic){
        return switch (tryLogic) {
            case "true", "on", "1", "enable" -> true;
            default -> false;
        };
    }

    public static boolean triggerFalse(String tryLogic){
        return switch (tryLogic) {
            case "false", "off", "0", "disable" -> true;
            default -> false;
        };
    }
}
