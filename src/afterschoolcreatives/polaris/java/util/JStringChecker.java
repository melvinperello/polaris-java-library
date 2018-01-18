package afterschoolcreatives.polaris.java.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Joemar String Checker class.
 *
 * @author Joemar
 */
public class JStringChecker {

    /**
     * checks the given string if all given char are letters only
     *
     * @param str
     * @return
     */
    public static boolean isAlpha(String str) {
        Pattern p = Pattern.compile("\\p{L}+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * checks the given string if all given char are numbers only
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern p = Pattern.compile("\\p{N}+");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * checks the given string if all given char are letters or numbers only
     *
     * @param str
     * @return
     */
    public static boolean isAlphaNumeric(String str) {
        Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return !m.find();
    }

    /**
     *
     * @param str string to be checked
     * @param acceptedCharacter char that will be valid for checking
     * @return
     */
    public static boolean isAlphaNumeric(String str, String... acceptedCharacter) {
        String regex = "a-z0-9";
        for (String c : acceptedCharacter) {
            regex += c;
        }
        System.out.println(regex);
        Pattern p = Pattern.compile("[^" + regex + "]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return !m.find();
    }

    /**
     * checks the given string if all given char are letters, numbers or with
     * space only
     *
     * @param str
     * @return
     */
    public static boolean isAlphaNumericSpace(String str) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return !m.find();
    }

    /**
     *
     * @param str string to be checked
     * @param acceptedCharacter char that will be valid for checking
     * @return
     */
    public static boolean isAlphaNumericSpace(String str, String... acceptedCharacter) {
        String regex = "a-z0-9 ";
        for (String c : acceptedCharacter) {
            regex += c;
        }
        System.out.println(regex);
        Pattern p = Pattern.compile("[^" + regex + "]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(str);
        return !m.find();
    }

}
