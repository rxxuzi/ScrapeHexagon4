package fast;

import java.io.File;

public class Del {
    private static final String PicDirPath = "./resources/pic/";
    private static final String LogDirPath = "./resources/log/";

    public static void allPicDelete() {
        //delete all files in the ./resources/ folder
        File[] files = new File(PicDirPath).listFiles();
        if(files != null){
            for(File file : files){
                if(file.delete()) System.out.println("Deleted " + file.getName());
                else System.out.println("Failed to delete " + file.getName());
            }
        }

    }

    public static int numberOfPic(){
        File[] files = new File(PicDirPath).listFiles();
        if (files == null) return 0;
        else return files.length;
    }

    public static int numberOfLog(){
        File[] files = new File(LogDirPath).listFiles();
        if (files == null) return 0;
        else return files.length;
    }

    public static void allLogDelete() {
        //delete all files in the ./resources/ folder
        File[] files = new File(LogDirPath).listFiles();
        if(files != null){
            for(File file : files){
                if(file.delete()) System.out.println("Deleted " + file.getName());
                else System.out.println("Failed to delete " + file.getName());
            }
        }
    }
}
