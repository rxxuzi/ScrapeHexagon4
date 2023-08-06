package global;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FIS {
    public static void main(String[] args) {
        String p = "./";

        boolean b = false;
        String s = "";
        for(int i = 0 ; i < 5 ; i ++ ){
            File dir = new File(p);
            if(dir.isDirectory()){
                System.out.println("d");
            }
            File[] list = dir.listFiles();
            System.out.println(dir.getAbsolutePath());
            for (File file : list) {
                System.out.print(file.getName() + "\t");
                if(file.getName().equals("pwa")){
                    b = true;
                    s = file.getAbsolutePath();
                    break;
                }
            }
            p = "./." + p;
        }

        File file = new File(s + "/README.md");

        FileReader fr = null;
        System.out.println(file.getPath());
        System.out.println(file.getParent());
        try {
            fr = new FileReader(file);
            BufferedReader  reader = new BufferedReader(fr);
            String line = "";
            while((line = reader.readLine()) != null){
                System.out.println(line);
            }
            reader.close();
            fr.close();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
