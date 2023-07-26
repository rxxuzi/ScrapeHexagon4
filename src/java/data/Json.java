package data;

import com.google.gson.*;
import global.GlobalProperties;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Json {
    public static Integer ERROR_COUNT = 0;
    public static void write(String filename, HashMap<String, List<String>> data) {
        String path  = GlobalProperties.JSON_DIR  +  filename + ".json";
        File file = new File(path);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fw;
        try {
            fw = new FileWriter(file);
            fw.write(gson.toJson(data));
            fw.close();
            System.out.println("Write to file successfully / Size Of List" + data.size());
        }catch (Exception e){
            e.printStackTrace();
            ERROR_COUNT++;
        }
    }

    private int id = 0 ;
    public void read(String filename){
        String path  = GlobalProperties.JSON_DIR  +  filename;
        File file = new File(path);
        FileReader fr;
        try {
            fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        BufferedReader reader;
        reader = new BufferedReader(fr);

        //Parses a JSON file and converts it to a JSON array
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

        System.out.println("Array Size -> " + jsonArray.size());

        //Get Random ID
        Random random = new Random();
        id = random.nextInt(jsonArray.size());

        //Retrieve the corresponding id: object from the JSON array
        JsonObject jsonObject = null;
        for (JsonElement element : jsonArray) {
            if (element.getAsJsonObject().get("id").getAsInt() == id) {
                jsonObject = element.getAsJsonObject();
                break;
            }
        }

        //Get the value of tag from the object with the corresponding id
//        if(jsonObject != null){
//            tag = jsonObject.get("tag").getAsString();
//            String category = jsonObject.get("category").getAsString();
//            System.out.println("id (" + id + ") -> " +tag + " (" + category + ")"); // example2
//        }

    }
}
