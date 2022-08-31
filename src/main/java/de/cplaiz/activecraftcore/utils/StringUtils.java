package de.cplaiz.activecraftcore.utils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils {

    private static final String IPV4_REGEX =
            "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                    "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";

    private static final Pattern IPv4_PATTERN = Pattern.compile(IPV4_REGEX);

    public static boolean isValidInet4Address(String ip) {
        if (ip == null) return false;
        Matcher matcher = IPv4_PATTERN.matcher(ip);
        return matcher.matches();
    }

    public static String combineArray(String[] args) {
        return combineArray(args, 0, args.length, " ");
    }

    public static String combineArray(String[] args, int start) {
        return combineArray(args, start, args.length, " ");
    }

    public static String combineArray(String[] args, int start, int stop) {
        return combineArray(args, start, stop, " ");
    }

    public static String combineArray(String[] args, int start, String splitter) {
        return combineArray(args, start, args.length, splitter);
    }

    public static String combineArray(String[] args, int start, int stop, String splitter) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = start; i < stop; i++)
            resultBuilder.append(i != start ? splitter + args[i] : args[i]);
        return resultBuilder.toString();
    }

    public static String combineList(List<String> args) {
        return combineList(args, 0, args.size(), " ");
    }

    public static String combineList(List<String> args, int start) {
        return combineList(args, start, args.size(), " ");
    }

    public static String combineList(List<String> args, int start, int stop) {
        return combineList(args, start, stop, " ");
    }

    public static String combineList(List<String> args, String splitter) {
        return combineList(args, 0, args.size(), splitter);
    }

    public static String combineList(List<String> args, int start, String splitter) {
        return combineList(args, start, args.size(), splitter);
    }

    public static String combineList(List<String> args, int start, int stop, String splitter) {
        StringBuilder resultBuilder = new StringBuilder();
        for (int i = start; i < stop; i++)
            resultBuilder.append(i != start ? splitter + args.get(i) : args.get(i));
        return resultBuilder.toString();
    }

    public static String remove(String target, String... replaced) {
        for (String s : replaced)
            target = target.replace(s, "");
        return target;
    }

    public static boolean anyEquals(String input, String... strings) {
        return Arrays.asList(strings).contains(input);
    }

    public static boolean anyEqualsIgnoreCase(String input, String... strings) {
        return Arrays.stream(strings).anyMatch(input::equalsIgnoreCase);
    }

    public static String toJadenCase(String input) {
        assert !input.isEmpty();
        String[] splitted = input.split(" ");
        return Arrays.stream(splitted)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
    }

    public static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }
}