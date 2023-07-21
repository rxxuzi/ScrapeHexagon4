package data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Objects;

public class Search {

    private static final String[] PATH = {
            "./data/GeneralPTag.json",
            "./data/ArtistTag.json",
            "./data/CopyrightTag.json",
            "./data/CharacterTag.json",
            "./data/GeneralTag.json"
    };

    public int id = 0;
    public String category;
    public String tag;
    public boolean isExist = false;

    /**
     * Searches for the tag in the JSON file
     * @param s tag to search for
     * @throws NullPointerException if the json file is null
     *
     */
    public Search(String s) {

        System.out.println("Searching for -> " + s);
        //jsonファイルパスの分だけ回す

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
                if (Objects.equals(element.getAsJsonObject().get("tag").getAsString(), s)) {
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
                }
                break;
            }
        }
        if (!isExist) {
            System.out.println("Not found");
        }

    }
}
