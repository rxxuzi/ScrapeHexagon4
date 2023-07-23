package crawler;

import global.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public final class Yelan {
    public static String http = "https://safebooru.donmai.us/";
    public static int MAX_COUNT = 1000;
    public static void main(String[] args) {
        Properties properties = new Properties();
        long  startTime = System.currentTimeMillis();
        try{
            URL url = new URL("https://safebooru.donmai.us/posts?page=1&tags=w_%28arknights%29");
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
            System.out.println("Content-Type: " + contentType);

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
            Document document = Jsoup.parse(htmlContent.toString());
            System.out.println(document.title());

            //get #posts img element
            Elements posts = document.getElementsByClass("post-preview-link");

//            for(var post : posts){
//                var p = post.attr("href");
//                String link = http + p ;
//                Downloader dl = new Downloader(link);
//                dl.run();
//            }
            AtomicReference<Downloader> downloader = new AtomicReference<>();
            // 並列処理for文
            IntStream.range(0, posts.size()).forEach(i -> {
                var p = posts.get(i).attr("href");
                String link = http + p ;
                downloader.set(new Downloader(link));
                downloader.get().run();
            });

            System.out.println("finish");
            System.out.println(Downloader.count.get() + " images downloaded");

        }catch (Exception e){
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}
