package crawler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import global.GlobalProperties;
import global.OpenHTML;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 * <pre>
 *     {@code
 *     Downloader downloader = new Downloader(srcURL);
 *     downloader.run(); //run the crawler
 *     }
 * </pre>
 *
 * Code snippet from OpenSRC.java
 *
 */
public final class Downloader extends Thread {
    private final static String fileDir = GlobalProperties.PIC_DIR;
    private final static String ext = GlobalProperties.FILE_FORMAT;
    private boolean saveTag = GlobalProperties.TAG2JSON;
    private URL url;
    private String fileName;

    public Downloader(String srcURL) {
        try{
            this.url = new URL(srcURL);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if(OpenSRC.isRunning.get()){
            try {
                Document document = OpenHTML.html(url);

                //get #image img element
                Element imageElement = document.getElementById("image");

                if (imageElement != null) {
                    String imageUrl = imageElement.attr("src");
//                System.out.println("Image URL: " + imageUrl);

                    fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                    // remove fileformat extension
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));

                    String fileFormat = fileFormat(imageUrl);
                    String filepath = fileDir + fileName + fileFormat;
                    // 画像をダウンロードして保存する
                    downloadImage(imageUrl, filepath);
                    System.out.println("Download image : " + url);
                } else {
                    System.out.println("Image not found.");
                }

                if(GlobalProperties.TAG2JSON){
                    tagList(document);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //image downloader
    private static void downloadImage(String imageUrl, String fileName) throws IOException {
        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        InputStream inputStream = connection.getInputStream();
        OutputStream outputStream = new FileOutputStream(fileName);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.close();
        inputStream.close();
        connection.disconnect();
    }
    // set fileFormat
    private static String fileFormat(String ex){
        if     (ex.endsWith("gif")) return ".gif";
        else if(ex.endsWith("webm"))return ".webm";
        else if(ex.endsWith("mp4")) return ".mp4";
        else if(ex.endsWith("mov")) return ".mov";
        else if(ex.endsWith("wmv")) return ".wmv";
        else return ext;
        
    }

    private final List<String> generalTagList = new ArrayList<>();
    private final List<String> characterTagList = new ArrayList<>();
    private final Map<String, List<String>> E = new HashMap<>();

    private void tagList (Document doc){
        Elements elements;
        Elements generalElements = doc.getElementsByClass("tag-type-0");
        Elements characterElements = doc.getElementsByClass("tag-type-4");

        for(Element general : generalElements){
            var e = general.getElementsByClass("search-tag");
            generalTagList.add(e.text());
        }

        for (Element character : characterElements){
            var e = character.getElementsByClass("search-tag");
            characterTagList.add(e.text());
        }

        E.put("character" , characterTagList);
        E.put("general" ,generalTagList);


        if(saveTag){
            String path  = GlobalProperties.JSON_DIR  +  fileName + ".json";
            File file = new File(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fw;
            try {
                fw = new FileWriter(file);
                fw.write(gson.toJson(E));
                fw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            saveTag = false;
        }
    }

}
