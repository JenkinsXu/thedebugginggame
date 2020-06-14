package io.jenkinsxu.github.findthebug.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BugNameGenerator {
    private static List<String> bugNames = new ArrayList<>(Arrays.asList(
            "ClassNotFoundException",
            "FileNotFoundException",
            "NullPointerException",
            "NumberFormatException",
            "RuntimeException",
            "ArithmeticException",
            "IOException",
            "InterruptedException",
            "NoSuchMethodException",
            "NoSuchFieldException"
    ));

    public static String getRandomBugName() {
        Random random = new Random();
        int index = random.nextInt(bugNames.size());
        return bugNames.get(index);
    }
}
