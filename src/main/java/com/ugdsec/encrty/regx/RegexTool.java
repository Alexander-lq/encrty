package com.ugdsec.encrty.regx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTool {
    public static boolean match(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void main(String[] args) {
        String regex = "^SELECT\\s+(?:SYSTEM_USER|CURRENT_USER|SESSION_USER|USER)\\(\\)\\s*;?$";
        String input1 = "SELECT SYSTEM_USER();";
        String input2 = "SELECT CURRENT_USER();";
        String input3 = "SELECT USER();";
        String input4 = "SELECT CURRENT_USER;";

        System.out.println(match(regex, input1)); // true
        System.out.println(match(regex, input2)); // true
        System.out.println(match(regex, input3)); // true
        System.out.println(match(regex, input4)); // false
    }
}
