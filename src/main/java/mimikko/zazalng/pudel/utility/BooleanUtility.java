package mimikko.zazalng.pudel.utility;

public class BooleanUtility {
    public static boolean toggleLogic(String tryLogic, boolean expected) {
        return switch (tryLogic.toLowerCase()) {
            case "true", "on", "1", "enable" -> expected;
            case "false", "off", "0", "disable" -> !expected;
            default -> false;
        };
    }

    public static boolean betweenNumeric(int test, int min, int max, boolean inclusive) {
        if (inclusive) {
            return test >= min && test <= max;
        } else {
            return test > min && test < max;
        }
    }

    public static boolean isNumeric(String test){
        return test != null && test.matches("\\d+");
    }
}
