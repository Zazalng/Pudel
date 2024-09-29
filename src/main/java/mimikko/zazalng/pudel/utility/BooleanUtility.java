package mimikko.zazalng.pudel.utility;

public class BooleanUtility {
    public static boolean toggleLogic(String tryLogic, boolean expected) {
        return switch (tryLogic.toLowerCase()) {
            case "true", "on", "1", "enable" -> expected;
            case "false", "off", "0", "disable" -> !expected;
            default -> false;
        };
    }
}
