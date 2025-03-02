package app.hyperskill.server;

import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbController {
    private static final String ERROR = "ERROR";
    private static final String OK = "OK";

    private final IDatabase db;
    private final Scanner scanner;
    private final Pattern commandPattern;

    public DbController(IDatabase db, Scanner scanner, Pattern commandPattern) {
        this.db = db;
        this.scanner = scanner;
        this.commandPattern = commandPattern;
    }

    public void run(){
        String input;
        do{
            input = scanner.nextLine();
            Matcher matcher = commandPattern.matcher(input);
            if(matcher.matches()){
                String command = matcher.group(1);
                int index = Integer.parseInt(matcher.group(2)) - 1;
                String text = matcher.group(3);
                switch (command){
                    case "set" -> System.out.println(db.set(text, index) ? OK : ERROR);
                    case "get" -> System.out.println(Objects.requireNonNullElse(db.get(index), ERROR));
                    case "delete" -> System.out.println(db.delete(index) ? OK : ERROR);
                    default -> System.out.println("Something terrible has happened.");
                }
            }
            else if("exit".equals(input)){
                break;
            }
            else{
                System.out.println("wrong input format");
            }
        }while(!"exit".equals(input));
    }
}
