package active;
import data.Json;

import java.util.*;
import java.util.ArrayList;

public class Suggestions {
    private final static List<String> words = Json.toArray();
    public String text;
    public static int score = -1;

    Suggestions(String s){

    }
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.print("入力してください: ");
        String input = scanner.next();
        scanner.close();
        // input words replace -> " " to "_"
        input = input.replaceAll("&", "_");
        System.out.println("入力された単語: " + input);
        String bestMatch = find(input);
        System.out.println("推測された単語: " + bestMatch);
    }

    /**
     * wordsのなかから単語を推測する
     * @param searchWord 入力した単語
     * @return 推測された単語
     */
    private static String find(String searchWord) {
        String word = searchWord.replaceAll("\\(", "").replaceAll("\\)", "");

        String[] words = word.split("_");

        System.out.println("WORDS SPLIT ->" + Arrays.toString(words) + " : WORD LENGTH -> " + words.length);
        
        if (words.length > 1) {
            System.out.println();

            List<String>[] lists = new ArrayList[words.length];
            //仮リスト
            List<String> tmp;
            for (int i = 0; i < words.length; i++) {
                lists[i] = findMatchList(words[i]);
            }
            tmp = findCommonElements(lists[0], lists[1]);

            if(words.length > 2) {
                for (int i = 2; i < words.length; i++) {
                    tmp = findCommonElements(tmp, lists[i]);
                }
            }

            return tmp.toString();
        }else {
            if(searchWord.length() > 3){
                String yel = findBestMatchString(searchWord);
                if (score <= 3){
                    System.out.println("Sorry ! this score is "+ score);
                    System.out.println("Maybe ->  ");
                    return findNearestString(yel); //TODO
                }else {
                    return yel;
                }
            }else{
                System.out.println("Your String is too short");
                return findNearestString(searchWord);
            }
        }
    }

    // リストの中から重複している単語をみつけてリストで返す
    private static List<String> findCommonElements(List<String> list1, List<String> list2) {
        // list1をHashSetに変換して重複を排除
        List<String> commonElements = new ArrayList<>();

        for (String element : list1) {
            if (list2.contains(element)) {
                commonElements.add(element);
            }
        }

        return commonElements;
    }

    /**
     * 特定の文字列を含む単語を抜き出す
     * @param searchString 探したい文字列
     * @return 一致した単語をまとめたList
     */
    private static List<String> findMatchList(String searchString) {
        List<String> op = new ArrayList<>();

        for (String word : words) {
            if (word.contains(searchString)) {
                op.add(word);
            }
        }

        return op;
    }

    @Deprecated
    private static List<String> findBestMatchList(String input) {
        int maxMatchingLength = -1;
        List<String> wordsWithSameLength = new ArrayList<>();

        for (String word : words) {
            int matchingLength = calculateMatchingLength(input, word);
            if (matchingLength > maxMatchingLength) {
                maxMatchingLength = matchingLength;
                wordsWithSameLength.clear();
                wordsWithSameLength.add(word);
            }else if (matchingLength == maxMatchingLength) {
                wordsWithSameLength.add(word);
            }
        }
        return wordsWithSameLength;
    }

    /**
     * 最も近い単語を見つける
     * Levenshtein距離法ではなく、単に連続した文字列のスコアでカウントする
     * @param searchString
     * @return
     */
    private static String findBestMatchString(String searchString) {
        int maxMatchingLength = -1;
        List<String> wordsWithSameLength = new ArrayList<>();

        for (String word : words) {
            int matchingLength = calculateMatchingLength(searchString, word);
            if (matchingLength > maxMatchingLength) {
                maxMatchingLength = matchingLength;
                wordsWithSameLength.clear();
                wordsWithSameLength.add(word);
            }else if (matchingLength == maxMatchingLength) {
                wordsWithSameLength.add(word);
            }
            score = maxMatchingLength;
        }
        return wordsWithSameLength.toString();
    }

    private static int calculateMatchingLength(String word1, String word2) {
        int maxLength = Math.min(word1.length(), word2.length());
        int matchingLength = 0;

        for (int i = 0; i < maxLength; i++) {
            if (word1.charAt(i) == word2.charAt(i)) {
                matchingLength++;
            } else {
                break;
            }
        }

        return matchingLength;
    }

    public static String findNearestString( String str) {
        //最も近い文字列
        String nearest = null;
        String original;
        int minDistance = Integer.MAX_VALUE;

        for (String s : words) {

            original = s;

            //空白とアンダーバーを削除
            s = s.replaceAll("[ _]", "");

            int distance = computeEditDistance(s, str);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = original;
            }

            if(minDistance == 0) {
                break;
            }
        }
        return nearest;
    }

    /**
     *
     * @param s1  配列内の文字列
     * @param s2　探す対象の文字列
     * @return 編集距離
     */
    private static int computeEditDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i][j - 1], dp[i - 1][j]));
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }
}
