package global;

import java.io.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class NotSearch {

    public static final String PATH = "./config/EXCLUSION.txt";

    public static List<String> getNotSearchWord()  {
        LinkedList<String> not = new LinkedList<>();
        File file = new File(PATH);
        try {

            if (file.exists()) {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);
                String line;
                while ((line = br.readLine()) != null) {
                    if(!line.startsWith("#")){
                        not.add(line);
                    }
                }
                br.close();
                fr.close();
            } else {
                System.out.println("Not found " + PATH);
            }
        }catch (IOException ignored) {
        }
        return not;
    }

    public static void main(String[] args) {
        System.out.println(getNotSearchWord().toString());
    }
}
