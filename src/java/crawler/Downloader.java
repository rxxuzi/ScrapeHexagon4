package crawler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import global.GlobalProperties;
import global.NotSearch;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLOutput;
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
public class Downloader extends Thread {
    private final static String fileDir = GlobalProperties.PIC_DIR;
    private final static String ext = GlobalProperties.FILE_FORMAT;
    private boolean saveTag = GlobalProperties.TAG2JSON;
    private URL url;
    private String fileName;
    private String imageUrl;
    private String source;
    private String rating;

    private static final List<String >NOT_WORD = NotSearch.getNotSearchWord();
    public static final AtomicInteger skipCnt = new AtomicInteger();

    private final List<String> generalTagList = new ArrayList<>();
    private final List<String> characterTagList = new ArrayList<>();
    private final Map<String, List<String>> E = new HashMap<>();



    private static final boolean hq = true;

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

                tagList(document);
                //NG WORD
                if(!skip()){
                    //get #image img element
                    Element imageElement;
                    boolean found = true;
                    if (hq) {
                        try{
                            imageElement = document.getElementsByClass("image-view-original-link").first();
                        }catch (Exception igException){
                            found = false;
                            System.out.println("Not found HQ image : " + url);
                            imageElement = document.getElementById("image");
                        }
                    }else{
                        imageElement = document.getElementById("image");
                    }

                    Element postsSRC = document.getElementById("post-info-source");
                    if(postsSRC != null){
                        source = postsSRC.getElementsByTag("a").first().attr("href");
                    }

                    Element rate = document.getElementById("post-info-rating");
                    if(rate != null){
                        rating = rate.text();
                    }


                    if (imageElement != null) {
                        if(hq && found){
                            imageUrl = imageElement.attr("href");
                        } else{
                            imageUrl = imageElement.attr("src");
                        }

                        // 画像をダウンロードして保存する
                        downloadImage(imageUrl, getFilePath());

                        System.out.println("Download image : " + url);
                    } else {
                        String p = document.getElementById("image").attr("src");
                        downloadImage(p,getFilePath(p));
                    }
                }else{
                    System.out.println("NG WORD");
                    skipCnt.getAndIncrement();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            saveJson();
        }
    }

    private String getFilePath(){
        fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // remove fileformat extension
        fileName = fileName.substring(0, fileName.lastIndexOf("."));

        String fileFormat = fileFormat(imageUrl);

        return fileDir + fileName + fileFormat;
    }
    private String getFilePath(String s){
        fileName = s.substring(s.lastIndexOf("/") + 1);

        // remove fileformat extension
        fileName = fileName.substring(0, fileName.lastIndexOf("."));

        String fileFormat = fileFormat(s);

        return fileDir + fileName + fileFormat;
    }


    private boolean skip(){
        for(String word : NOT_WORD){
            if(generalTagList.contains(word) || characterTagList.contains(word)){
                return true;
            }
        }
        return false;
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

    private void saveJson() {
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
        }
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

    private void tagList (Document doc){
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

        List<String> Info = new ArrayList<>();
        Info.add("Page Link : " + this.url.toString());
        Info.add("Image Link : " + imageUrl);
        Info.add("Source Link : " + source);
        Info.add("Rating : " + rating);

        E.put("Link" , Info);
        E.put("character" , characterTagList);
        E.put("general" ,generalTagList);
    }

}
