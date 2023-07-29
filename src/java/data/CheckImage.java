package data;

import java.io.File;

public class CheckImage {
    public static void main(String[] args) {
        File dir = new File("./output/pics");
        File[] files = dir.listFiles();
        assert files != null;
        System.out.println(files.length);
    }
    public void check(){
        File dir = new File("./output/pics");
        File[] files = dir.listFiles();
        assert files != null;
        System.out.println(files.length);
    }
}
