package crawler;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 */
public class Downloader {
//    private String url = "https://hijiribe.donmai.us/posts/6483817?q=mostima_%28arknights%29";
    private static String saveDir = "./resources/pic/";
    URL url;
    public Downloader(String src) {
        try{
            this.url = new URL(src);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        System.out.println("Download image : " + src);

    }
    public void run() {
        try {
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
            System.out.println("Content-Type: " + contentType);

            // Input Stream
            DataInputStream dataInStream
                    = new DataInputStream(
                    openConnection.getInputStream());

            // Read HTML content
            BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            //1行ずつhtmlを読み取る
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();
//            System.out.println(htmlContent.toString());

            Document document = Jsoup.parse(htmlContent.toString());
            System.out.println(document.title());

            //get #image img element
            Element imageElement = document.getElementById("image");

            if (imageElement != null) {
                String imageUrl = imageElement.attr("src");
                System.out.println("Image URL: " + imageUrl);

                // 画像をダウンロードして保存する
                downloadImage(imageUrl, "downloaded_image.jpg");
                System.out.println("Image downloaded successfully.");
            } else {
                System.out.println("Image not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

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
    }


}
