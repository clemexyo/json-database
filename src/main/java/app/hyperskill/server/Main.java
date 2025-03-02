package app.hyperskill.server;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern pattern = Pattern.compile("^(set|get|delete) (\\d+)(?: (.+))?$");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbController dbController = new DbController(new ArrayDatabase(1000), scanner, pattern);
        dbController.run();
        scanner.close();
    }
}

