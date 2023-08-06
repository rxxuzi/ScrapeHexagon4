package data;

import global.GlobalProperties;

import java.io.File;

public final class CheckImage {
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
    public int dirLength(){
        File dir = new File(GlobalProperties.PIC_DIR);
        File[] files = dir.listFiles();
        assert files != null;
        return files.length;
    }
}
