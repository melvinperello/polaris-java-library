package afterschoolcreatives.polaris.java.util;

/**
 * Joemar String Checker class.
 *
 * @author Joemar
 */
public class JStringChecker {
    
    /**
     * checks the given string if all given char are letters
     * @param str
     * @return true if all given char in string are letters
     */
    public static boolean areAlpha(String str) {
        return str.matches("\\p{L}+");
    }
    
    /**
     * checks the given string if all given char are numbers
     * @param str
     * @return true if all given char in string are numbers
     */
    public static boolean areNumeric(String str) {
        return str.matches("\\p{N}+");
    }
}
