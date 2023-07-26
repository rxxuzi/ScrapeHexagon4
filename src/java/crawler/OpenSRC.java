package crawler;

import fast.Tag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import global.GlobalProperties;
import javax.net.ssl.SSLHandshakeException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

/**
 * This class is a crawler of the OpenSRC website.
 * It can be used to download images from the website.
 *
 * <p>The following example illustrates how to use this class:
 *
 * <pre>
 *     {@code
 *       OpenSRC osrc = new OpenSRC(); //create a new crawler
 *       osrc.setTag(exampleSearchedTag); //set tag
 *       osrc.run();//run the crawler
 *     }
 * </pre>
 * @author rxxuzi
 * @version 1.5
 */
public final class OpenSRC {

    public final static String http = GlobalProperties.DOMAIN;
    public final static String BASIC_URL = GlobalProperties.DOMAIN+"posts?page=";
    public final static int MAX_IMG_CNT = Math.min(Main.IMG , GlobalProperties.MAX_IMG_CNT);
    public static int PAGE_COUNT = 1;
    private static final String BASIC_TAG = "&tags=";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    private String TAG = "exusiai_%28arknights%29";

    public String getTag() {
        return TAG;
    }

    public void setTag(String TAGNAME) {
        this.TAG = Tag.translate(TAGNAME);
    }
    public void run() {
        do {
            sendPage(BASIC_URL + PAGE_COUNT + BASIC_TAG  + TAG);
            PAGE_COUNT++;
        }while (isRunning.get());
        System.out.println("Last finish");
    }

    public void sendPage(String page){
        try{
            URL url = new URL(page);
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

            AtomicReference<Downloader> downloader = new AtomicReference<>();

            boolean fullLoop =  Downloader.count.get() + posts.size() < MAX_IMG_CNT;
            System.out.println("fullLoop : " + fullLoop + " count : " + Downloader.count.get() + " posts.size() : " + posts.size() + " MAX_IMG_CNT : " + MAX_IMG_CNT);
            if(posts.size() == 0 ){
                isRunning.set(false);
                return;
            }
            if(fullLoop){
                // 並列処理for文
                IntStream.range(0, posts.size()).forEach(i -> {
                    var p = posts.get(i).attr("href");
                    String link = http + p ;
                    if (Downloader.count.get() < MAX_IMG_CNT ) {
                        downloader.set(new Downloader(link));
                        downloader.get().run();
//                        System.out.println("MediaDownloader count : " + MediaDownloader.count.get());
                    }
                });
            }else {
                int n =  MAX_IMG_CNT - Downloader.count.get();
                // 並列処理for文
                IntStream.range(0, n).forEach(i -> {
                    var p = posts.get(i).attr("href");
                    String link = http + p ;
                    if (Downloader.count.get() < MAX_IMG_CNT ) {
                        downloader.set(new Downloader(link));
                        downloader.get().run();
//                        System.out.println("MediaDownloader count : " + MediaDownloader.count.get());
                    }
                });
            }

            if(Downloader.count.get() >= MAX_IMG_CNT){
                isRunning.set(false);
                System.out.println("finish");
                Thread.sleep(1000);
            }

            System.out.println(Downloader.count.get() + " images downloaded");

        }catch (FileNotFoundException e){
            System.out.println("FileNotFoundException");
            e.printStackTrace();
        }catch (SSLHandshakeException e){
            System.out.println("SSLHandshakeException");
            e.printStackTrace();
        } catch (ProtocolException e) {
            System.out.println("ProtocolException");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IOException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
