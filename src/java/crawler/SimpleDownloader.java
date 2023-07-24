package crawler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import global.GlobalProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 */
public final class SimpleDownloader {
    private final static String fileDir = GlobalProperties.PIC_DIR;
    private final static String ext = GlobalProperties.FILE_FORMAT;
    public static final AtomicInteger count = new AtomicInteger();
    private static String path = "https://danbooru.donmai.us/posts/6379231?q=mostima_%28arknights%29+";

    public static void main(String[] args){

        OpenSRC.isRunning.set(count.get() < OpenSRC.MAX_IMG_CNT);
        if(OpenSRC.isRunning.get()){
            try {
                URL url = new URL(path);
                // HTTP URL Connection
                HttpURLConnection openConnection =
                        (HttpURLConnection) url.openConnection();
                openConnection.setAllowUserInteraction(false);
                openConnection.setInstanceFollowRedirects(true);
                openConnection.setRequestMethod("GET");
                openConnection.connect();

                int httpStatusCode = openConnection.getResponseCode();
                if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("HTTP Status " + httpStatusCode);
                }

                String contentType = openConnection.getContentType();
//            System.out.println("Content-Type: " + contentType);

                // Input Stream
                DataInputStream dataInStream
                        = new DataInputStream(
                        openConnection.getInputStream());

                // Read HTML content
                BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
                StringBuilder htmlContent = new StringBuilder();
                String line;
                //Read html one line at a time
                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line);
                }
                reader.close();
//            System.out.println(htmlContent.toString());
                Document document = Jsoup.parse(htmlContent.toString());
//            System.out.println(document.title());

                if(GlobalProperties.TAG2JSON){
                    tagList(document);
                }

                //get #image img element
                Element imageElement = document.getElementById("image");

                if (imageElement != null) {
                    String imageUrl = imageElement.attr("src");
//                System.out.println("Image URL: " + imageUrl);

                    String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                    // remove fileformat extension
                    fileName = fileName.substring(0, fileName.lastIndexOf("."));
                    String fileFormat = fileFormat(imageUrl);
                    String filepath = fileDir + fileName + fileFormat;
                    // 画像をダウンロードして保存する
                    downloadImage(imageUrl, filepath);
                    count.set(count.get()+1);
                } else {
                    System.out.println("Image not found.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
//            System.out.println(e.getMessage());
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

//    static LinkedList<List> E = new LinkedList<>();

    private static void tagList (Document doc){

        List<String> generalTagList = new ArrayList<>();
        List<String> characterTagList = new ArrayList<>();

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

//        E.add(generalTagList);
//        E.add(characterTagList);
//        save();

    }

//    public static void save() {
//        String filename = "taglist_" + count.get() + ".json";
//        String filePath = GlobalProperties.JSON_DIR + filename;
//        File file = new File(filePath);
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        try {
//            FileWriter fw = new FileWriter(file);
//            fw.write(gson.toJson(E));
//            fw.close();
//            System.out.println("Write to file successfully / Size Of List" + E.size());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
