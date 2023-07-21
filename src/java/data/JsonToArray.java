package data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class JsonToArray {
    private static final String[] PATH = {
            "./data/GeneralPTag.json",
            "./data/ArtistTag.json",
            "./data/CopyrightTag.json",
            "./data/CharacterTag.json",
            "./data/GeneralTag.json"
    };
    public static List<String> data = new ArrayList<>();

    public static int ArraySize = 0;

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
}
