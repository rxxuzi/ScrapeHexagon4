package crawler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import global.GlobalProperties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 */
public final class Downloader  {
    private final static String fileDir = GlobalProperties.PIC_DIR;
    private final static String ext = GlobalProperties.FILE_FORMAT;
    private boolean saveTag = false;
    public static final AtomicInteger count = new AtomicInteger();
    private URL url;

    public Downloader(String srcURL) {
        try{
            this.url = new URL(srcURL);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            System.out.println(count.get() + " : Download image : " + srcURL);
        }
    }

    public void run() {
        OpenSRC.isRunning.set(count.get() < OpenSRC.MAX_IMG_CNT);
        if(OpenSRC.isRunning.get()){
            try {
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

    private List<String> generalTagList = new ArrayList<>();
    private List<String> characterTagList = new ArrayList<>();
    private Map<String, List> E = new HashMap();

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
            String path  = GlobalProperties.JSON_DIR + doc.title() + count.get() +".json";
            File file = new File("./resources/test.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(gson.toJson(E));
                fw.close();
                System.out.println("Write to file successfully / Size Of List" + E.size());
            }catch (Exception e){
                e.printStackTrace();
            }
            saveTag = false;
        }
    }

    class DataObject{

    }
}
