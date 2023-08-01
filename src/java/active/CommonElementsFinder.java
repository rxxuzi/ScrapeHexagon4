package active;

import java.util.*;

public class CommonElementsFinder {
    public static void main(String[] args) {
        ArrayList<String> E1 = new ArrayList<>(Arrays.asList("apple", "banana", "orange", "lime"));
        ArrayList<String> E2 = new ArrayList<>(Arrays.asList("red", "yellow", "orange", "green", "blue","lime"));

        List<String> commonElements = findCommonElements(E1, E2);
        System.out.println("共通の要素: " + commonElements);
    }

    private static List<String> findCommonElements(List<String> list1, List<String> list2) {
        List<String> commonElements = new ArrayList<>();

        for (String element : list1) {
            if (list2.contains(element)) {
                commonElements.add(element);
            }
        }

        return commonElements;
    }
}

