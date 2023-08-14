package _base18;

import crawler.OpenHTML;
import global.GlobalProperties;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Downloader18 extends Thread{
    private final static String baseDir = GlobalProperties.BASE_DIR;
    private String fileDir = GlobalProperties.PIC_DIR;
    private final static String ext = GlobalProperties.FILE_FORMAT;
    private boolean saveTag = GlobalProperties.TAG2JSON;
    private URL url;
    private String fileName;
    private String imageUrl;
    Document document;
    boolean makeddir = false;
    boolean saved = false;
    public static final String[] skips = new String[]{
            "skip", "gay" , "scat"
    };
    public static final String[] and = new String[]{

    };

    private boolean saveImage = true;
    List<String> tags = new LinkedList<>();

    Downloader18(String s){
        try {
            this.url = new URL(s);
            document = OpenHTML.html(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    Downloader18(String s, boolean saveImage){
        this.saveImage = saveImage;
        try {
            this.url = new URL(s);
            document = OpenHTML.html(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        try {
            Elements pageTitle = document.getElementsByTag("h1");
            if(!skip()){
                if(andSearch()){

                    mkdir(pageTitle.get(0).text());

                    Element main = document.getElementById("post-hentai");

                    if (main != null)    {
                        Elements pics = main.getElementsByTag("img");
                        for (Element pic : pics){
                            imageUrl = pic.attr("src");
                            fileName = pic.attr("alt");
                            if(saveImage){
                                saved = downloadImage(imageUrl , getFilePath());
                            }
                        }
                    }
                }
            }

            if(saved){
                System.out.println(" SAVE " + pageTitle.get(0).text());
            }else {
                System.out.println(" SKIP " + pageTitle.get(0).text());
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //image downloader
    private static boolean downloadImage(String imageUrl, String fileName) throws IOException {
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
        return true;
    }

    private boolean andSearch(){
        boolean b = false;
        if(and.length >= 1){
            for (String a : and){
                if(tags.contains(a)){
                    b = true;
                }else {
                    b = false;
                    break;
                }
            }
        }else {
            b = true;
        }
        saved = b;
        return b;
    }

    public boolean skip(){
        Elements tagTable = document.getElementsByClass("post-tag-table");
        for (Element tag : tagTable){
            Elements es = tag.getElementsByTag("a");
            for (Element e : es){
                String np = e.text();
                for(String word : skips){
                    if (Objects.equals(word, np)){
                        System.out.println("Skip!");
                        saved = false;
                        return true;
                    }
                }
                tags.add(np);
            }
        }
        return false;
    }

    private void mkdir(String altName){
        File dir = new File(baseDir + altName);
        if(!dir.exists()){
            if(dir.mkdir()){
//                System.out.println("Dir -> " + dir.getName());
//                fileDir = dir.getPath();
                makeddir = true;
            }
        }
        fileDir = baseDir + altName + "/" ;
    }
    private String getFilePath(){
        fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // remove fileformat extension
        fileName = fileName.substring(0, fileName.lastIndexOf("."));

        String fileFormat = fileFormat(imageUrl);

        return fileDir + fileName + fileFormat;
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

}

