package mimikko.zazalng.puddle.utility;

public class IntegerUtility {
    public static int randomInt(int max){
        return (int) (Math.random()*(max+1));
    }

    public static int randomInt(int min, int max){
        return (int) (Math.random()*(max - min + 1) + min);
    }
}
