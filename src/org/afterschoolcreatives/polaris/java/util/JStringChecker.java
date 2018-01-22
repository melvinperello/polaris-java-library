/**
 *
 * Polaris Java Library - Afterschool Creatives "Captivating Creativity"
 *
 * Copyright 2018 Joemar De La Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package org.afterschoolcreatives.polaris.java.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Joemar String Checker class.
 *
 * @author Joemar
 *
 * @deprecated This class was deprecated since 01/19/2018
 * @see StringTools please use this class instead.
 *
 * @afterschoolcreatives
 *
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
