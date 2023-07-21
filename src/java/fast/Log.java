package fast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;

public class Log{

    public static int l =  0;
    private static final String DirPath = "./resources/Log/";

    public static void write(String content) {
        File file = new File(DirPath  +"Get_"+ l + ".log");
        try {
            FileWriter fw = new FileWriter(file , true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.newLine();
            bw.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void error(String content) {
        File file = new File(DirPath + "error-log"  + ".log");
        try {
            //追記可能
            FileWriter fw = new FileWriter(file , true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(getTime() +":"+ content);
            bw.newLine();
            bw.close();
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getTime() {
        //get mm/dd/yyyy hh:mm:ss
        String time = LocalDateTime.now().toString().replace(":", "-");
        // "."以降の文字を削除
        return time.substring(0, time.indexOf("."));
    }

    public static void clean(){
        Del.allLogDelete();
    }

    public static int getLogNum(){
        File[] files = new File(DirPath).listFiles();
        int num ;
        if(files == null){
            num = 0;
        }else {
            num = files.length;
        }
        return num;
    }
}
