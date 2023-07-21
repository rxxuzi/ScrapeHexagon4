package data;

import java.util.*;


public class Predict {
    static List<String> options = JsonToArray.toArray();
//    static String[] options = ReadFromJson.getJsonArray(0);
    static int[] score = new int[options.size()];

    static int SCORE;
    public String text;


    public Predict(String str) {
        String nearest = findNearestString(str);
        text = nearest;
        System.out.println(str + " is this ? " + nearest);
        System.out.println("SCORE : " + (100 - SCORE) + " : MAX 100");
    }

    public static String findNearestString( String str) {
        //最も近い文字列
        String nearest = null;
        String original;
        int minDistance = Integer.MAX_VALUE;

        for (String s : options) {

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
//            if(minDistance < 10)
//                System.out.println(s + "->" + distance);
        }
//            System.out.println(s + "->" + distance);

        SCORE = minDistance;
//        System.out.println(minDistance);

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
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i][j - 1], dp[i - 1][j]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }

    /**
     *
     * @param str 探す対象の文字列
     * @return 最も近い文字列
     */
    @Deprecated
    public static String find(String str) {
        int point = 100;
        for (int i = 0 ; i < options.size(); i++) {
            String option = options.get(i);
            for (int j = 0; j < option.length(); j++) {
                if (str.indexOf(option.charAt(j)) != -1) {
                    score[i]+= point - j;
                    option = option.replaceFirst(String.valueOf(option.charAt(j)), "?");
                }
//                System.out.println(option);
            }
        }

        int maxScore = 0;
        int maxIndex = 0;

        for (int i = 0; i < score.length; i++) {
            if (score[i] > maxScore) {
                maxScore = score[i];
                maxIndex = i;
            }
        }

        return options.get(maxIndex);
    }

    /**
     * アナグラムにする(test用)
     * @param s 入力された文字列
     * @return アナグラム化した文字列
     */
    @Deprecated
    public static String anagram(String s) {
        int l = s.length();
        char[] c = s.toCharArray();
        for (int i = 0; i < l; i++) {
            Random r = new Random();
            int j = r.nextInt(l);
            char tmp = c[i];
            c[i] = c[j];
            c[j] = tmp;
        }
        return Arrays.toString(c);
    }
}
