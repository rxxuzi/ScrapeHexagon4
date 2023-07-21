package data;

import com.google.gson.*;

import java.io.*;
import java.util.Random;

/**
 * dataディレクトリ内のjsonファイルからランダムにタグとidを抜き取る
 */
public class ReadFromJson {
    private static final String[] PATH = {
            "./data/GeneralPTag.json",
            "./data/ArtistTag.json",
            "./data/CopyrightTag.json",
            "./data/CharacterTag.json",
            "./data/GeneralTag.json"
    };
    public int id = 0;
    public String tag;

    /**
     *
     * @param p pathの種類 (0 ~ 4)
     */
    public ReadFromJson(int p) {
        //Read Json file
        File file = new File(PATH[p]);
        System.out.println("Json -> " + file.getPath());
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
        if(jsonObject != null){
            tag = jsonObject.get("tag").getAsString();
            String category = jsonObject.get("category").getAsString();
            System.out.println("id (" + id + ") -> " +tag + " (" + category + ")"); // example2
        }
    }


}
