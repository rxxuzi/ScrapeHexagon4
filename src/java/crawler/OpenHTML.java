package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * このクラスはHTTP GETをするクラス
 * @author rxxuzi
 */
public final class OpenHTML {
    public static final String page = "https://www.java.com/ja/";
    public static boolean isRunning = true;

    public static String Page(URL url) {
        StringBuilder htmlContent;
        try {
            // HTTP URL Connection
            HttpURLConnection openConnection =
                    (HttpURLConnection) url.openConnection();
            openConnection.setAllowUserInteraction(false);
            openConnection.setInstanceFollowRedirects(true);
            // GET Request
            openConnection.setRequestMethod("GET");
            // Connect
            openConnection.connect();

            /*
              HTTP Status Code
              200 : OK
              400 : Bad Request
             */
            int httpStatusCode = openConnection.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP Status " + httpStatusCode);
            }
            // Input Stream
            DataInputStream dataInStream = new DataInputStream(openConnection.getInputStream());

            // Read HTML content
            BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
            htmlContent = new StringBuilder();
            String line;
            //Read html one line at a time
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return htmlContent.toString();
    }


    public Document html(String page) {
        URL url;
        try {
            url = new URL(page);
        }catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return Jsoup.parse(Page(url));
    }

    public static Document html(URL url) {
        return Jsoup.parse(Page(url));
    }

}
