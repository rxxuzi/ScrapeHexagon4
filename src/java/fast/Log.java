package fast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Log{

    public static int length =  0;
    private static final String DirPath = "./resources/Log/";
    ArrayList<String> log = new ArrayList<>();

    public static void write(String content) {
        File file = new File(DirPath  +"Get_"+ length + ".log");
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

    public static void error(Exception e) {
        File file = new File(DirPath + "error-log"  + ".log");
        try {
            //追試可否
            FileWriter fw = new FileWriter(file , true);
            BufferedWriter bw = new BufferedWriter(fw);
            //stack-trace
            //get method info
            bw.newLine();
            bw.write(getTime() +":"+ e.getStackTrace()[0].getClassName());
            bw.newLine();
            //simple class name
            bw.write(getTime() +":"+ e.getStackTrace()[0].getMethodName());
            bw.newLine();
            //get line number
            bw.write(getTime() +":"+ e.getStackTrace()[0].getLineNumber());
            //get message
            bw.newLine();

            bw.write("#  " + getTime() +":"+ e.getMessage());
            bw.newLine();
            bw.close();
            fw.close();
        }catch (Exception ex){
            ex.printStackTrace();
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
