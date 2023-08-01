package crawler;

import fast.WordMatcher;

import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        WordMatcher wordMatcher = new WordMatcher();
        Scanner scanner = new Scanner(System.in);
        System.out.print("入力してください: ");
        String input = scanner.nextLine();
        System.out.println("入力された単語: " + input);
        String bestMatch = wordMatcher.find(input);
        System.out.println("推測された単語: " + bestMatch);
    }
}
