package data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteToJson {

    public static final ArrayList<Data> dataList = new ArrayList<>();
    private static final String Path = "./data/";
    public static String file = "sample.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String[] Category = {
            "General",
            "Artist",
            "Nothing",
            "Copyright",
            "Character",
            "Meta"
//            General -> type - 0
//            Artist -> type -1
//            Copyright -> type - 3
//            Character -> type - 4
    };

    public static void add(int tagCounter, String tag , int posts , int categoryType) {
        //for example
        dataList.add(new Data(tagCounter, tag, posts ,  Category[categoryType]));
    }

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
