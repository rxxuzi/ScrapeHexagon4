package crawler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserInterface {
    public static final ArrayList<Data> dataList = new ArrayList<>();
    private static final String Path = "./data/";
    public static String file = "sample.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save() {
        String fileName = Path + file;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fw = new FileWriter(fileName);
            fw.write(gson.toJson(dataList));
            fw.close();
            System.out.println("Write to file successfully / Size Of List" + dataList.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
