package data;

import com.google.gson.*;
import global.GlobalProperties;

import java.io.*;
import java.util.*;
import java.util.Random;

public class Json {

    private static final String[] PATH = {
            "./data/GeneralPTag.json",
            "./data/ArtistTag.json",
            "./data/CopyrightTag.json",
            "./data/CharacterTag.json",
            "./data/GeneralTag.json"
    };

    public static final int JsonSize = PATH.length;

    public Json() {
        toArray();
        System.out.println("Json Array Size -> " + ArraySize);
        System.out.println("Json Array Size -> " + data.size());
    }
    public static List<String> data = new ArrayList<>();

    public static int ArraySize = 0;

    public static String getPath(int p){
        return PATH[p];
    }

    public static List<String> toArray() {

        for (String s : PATH) {

            File file = new File(s);

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

            ArraySize += jsonArray.size();

            //Json to Array
            for (int j = 0; j < jsonArray.size(); j++) {
                data.add(jsonArray.get(j).getAsJsonObject().get("tag").getAsString());
            }
        }
        return data;
    }


    public boolean isExist = false;
    public int  id;
    public String tag;
    public String category;
    public void search(String searchWord) {


        for (String p : PATH) {
            File file = new File(p);
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


            //Retrieve the corresponding id: object from the JSON array
            JsonObject jsonObject = null;

            for (JsonElement element : jsonArray) {
                if (Objects.equals(element.getAsJsonObject().get("tag").getAsString(), searchWord)) {
                    isExist = true;
                    jsonObject = element.getAsJsonObject();
                    break;
                }
            }

            if (isExist) {
                if (jsonObject != null) {
                    id = jsonObject.get("id").getAsInt();
                    tag = jsonObject.get("tag").getAsString();
                    category = jsonObject.get("category").getAsString();
                    System.out.println("id -> " + id);
                    System.out.println("tag -> " + tag);
                    System.out.println("category -> " + category);
                }
                break;
            }
        }
        if (!isExist) {
            System.out.println("Not found");
        }
    }

    public static boolean isExist(String searchWord) {
        boolean isExist = false;
        for (String p : PATH) {
            File file = new File(p);
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

            for (JsonElement element : jsonArray) {
                if (Objects.equals(element.getAsJsonObject().get("tag").getAsString(), searchWord)) {
                    return true;
                }
            }

        }
        return isExist;
    }

    public static String getRandomTag() {
        Random random = new Random();
        int index = random.nextInt(Json.toArray().size());
        return data.get(index);
    }
}
