package global;

import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.jsoup.nodes.Document ;

public class OpenHTML {
    public static final String page = "https://www.java.com/ja/";
    public static boolean isRunning = true;


    public Document html(String page) {
        URL url;
        try {
            url = new URL(page);
        }catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Document html = null;

        if (isRunning) {
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
                // Input Stream
                DataInputStream dataInStream = new DataInputStream(openConnection.getInputStream());

                // Read HTML content
                BufferedReader reader = new BufferedReader(new InputStreamReader(openConnection.getInputStream()));
                StringBuilder htmlContent = new StringBuilder();
                String line;
                //Read html one line at a time
                while ((line = reader.readLine()) != null) {
                    htmlContent.append(line);
                }
                reader.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return html;
    }

}
