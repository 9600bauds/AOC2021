package org.aoc2021.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.*;

public class Utils {

    public static List<String> SplitInputByCommas(String filename){
        return Input2List(filename, "(?:,)");
    }
    public static List<String> SplitInputByEmptyLines(String filename){
        return Input2List(filename, "(?:\\n|\\r\\n){2}");
    }
    public static List<String> SplitInputByLinebreaks(String filename){
        return Input2List(filename, "(?:\\n|\\r\\n)");
    }

    public static String Filename2String(String filename) {
        String fullPath = System.getProperty("user.dir") + "/src/inputs/" + filename + ".txt";
        Path fileName = Path.of(fullPath);
        try {
            return Files.readString(fileName);
        } catch (IOException e) {
            throw new RuntimeException("Invalid filename!");
        }
    }

    public static List<String> Input2List(String filename, String delimiter) {
        List<String> listOfStrings = new ArrayList<>();

        String fullPath = System.getProperty("user.dir") + "/src/inputs/" + filename + ".txt";
        File inputFile = new File(fullPath);
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(inputFile)).useDelimiter(delimiter);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found!");
        }
        String str;
        while (sc.hasNext()) {
            str = sc.next();
            listOfStrings.add(str);
        }
        //Remove last line if empty
        if(Objects.equals(listOfStrings.get(listOfStrings.size() - 1), "")){
            listOfStrings.remove(listOfStrings.size()-1);
        }
        return listOfStrings;
    }

    public static String[] GetAllRegexMatches(String input, String regex){
        return Pattern.compile(regex)
                .matcher(input)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
    }

    public static String GetFirstRegexMatch(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static boolean isFullyUppercase(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isUpperCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isFullyLowercase(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isLowerCase(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * You know how factorials go like, 5!=5*4*3*2*1? Triangular numbers are the same but with +. So, TriangularNum(5) = 5+4+3+2+1.
     */
    public static int TriangularNum(int num){
        return (num*(num+1))/2;
    }

    /**
     * You know how factorials go like, 5!=5*4*3*2*1? Triangular numbers are the same but with +. So, TriangularNum(5) = 5+4+3+2+1.
     * This calculates the triangular "root" of a number. So if TriangularNum(5) = 5+4+3+2+1 = 15, then TriangularRoot(15) = 5.
     */
    public static double TriangularRoot(int num) {
        return (-1 + Math.sqrt(1 + 8 * num)) / 2;
    }

    /**
     * Integer division, except it rounds up instead of down.
     */
    public static int DivideAndRoundUp(int dividend, int divisor){
        return (dividend - 1) / divisor + 1;
    }
    public static String NormalizeLineSeparators(String s){
        return s.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
    }

    static final Map<Character, String> hex2binmap = Map.ofEntries(
            Map.entry('0', "0000"), Map.entry('1', "0001"), Map.entry('2', "0010"), Map.entry('3', "0011"),
            Map.entry('4', "0100"), Map.entry('5', "0101"), Map.entry('6', "0110"), Map.entry('7', "0111"),
            Map.entry('8', "1000"), Map.entry('9', "1001"), Map.entry('A', "1010"), Map.entry('B', "1011"),
            Map.entry('C', "1100"), Map.entry('D', "1101"), Map.entry('E', "1110"), Map.entry('F', "1111")
    );
    public static String Hex2Bin(String hex){
        StringBuilder out = new StringBuilder();
        //out.append(Long.toBinaryString(Long.parseLong(hex, 16))); //Would not work for long numbers, gave wrong results for 04005AC33890 because it trimmed leading zeroes
        char[] chars = hex.toCharArray();
        for(char c : chars) {
            out.append(hex2binmap.get(c));
        }
        return out.toString();
    }

    /**
     * Adds to a value that wraps around back to a minimum value whenever it exceeds a maximum value.
     * For example, you can have a value that always stays between 10 and 50, looping back around when it goes out of bounds.
     * @param num The number to use as base
     * @param min The minimum value of said number
     * @param max The maximum value of said number
     * @param increase How much to increase said number by
     * @return The base number, modified
     */
    public static int SumToLoopingValue(int num, int min, int max, int increase){
        return ((num - min + increase) % (max - min + 1)) + 1;
    }

    /**
     * Adds to a value that wraps around back to 1 whenever it exceeds a maximum value.
     * For example, you can have a value that always stays between 1 and 100, looping back around when it goes out of bounds.
     * @param num The number to use as base
     * @param max The maximum value of said number
     * @param increase How much to increase said number by
     * @return The base number, modified
     */
    public static int SumToLoopingValue(int num, int max, int increase){
        return ((num - 1 + increase) % max) + 1;
    }

    /**
     * What it sounds like. Faster than doing it with strings.
     */
    public static boolean containsZero(long num) {
        if(num == 0)
            return true;

        if(num < 0)
            num = -num;

        while(num > 0) {
            if(num % 10 == 0)
                return true;
            num /= 10;
        }
        return false;
    }

    /**
     * Returns the digit at the specified index (starting from 0 for the leftmost digit) in the given number.
     * @param number The number to extract the digit from.
     * @param digitIndex The index of the digit to extract (starting from 0 for the leftmost digit).
     * @return The digit at the specified index.
     */
    public static int digit(Double number, int digitIndex) {
        // Compute the position of the desired digit from the right (starting from 0).
        int digitPositionFromRight = (int) (Math.log10(number) - digitIndex);

        // Shift the desired digit to the ones place by dividing the number by 10 raised to
        // the power of the position from the right, and then take the remainder when divided by 10.
        return (int) ((number / Math.pow(10, digitPositionFromRight)) % 10);
    }

    /**
     * Turns a Long into a stack of integers, representing its digits, with the most significant digits at the head of the stack.
     */
    public static Stack<Integer> longToStack(Long number) {
        Stack<Integer> stack = new Stack<>();
        while (number != 0) {
            int digit = (int) (number % 10);
            stack.push(digit);
            number /= 10;
        }
        return stack;
    }

    /**
     * For whatever god-forsaken reason, % is "remainder" and not "modulo" in Java, so -2 % 6 = -2. This is effectively REAL modulo.
     */
    public static int RealModulo(int number, int modulo)
    {
        int result = number % modulo;
        if (result < 0)
        {
            result += modulo;
        }
        return result;
    }
}
