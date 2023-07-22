package crawler;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloaderMain {
    public static void main(String[] args) {
        try {

            URL url = new URL("https://hijiribe.donmai.us/posts/6489557?q=yelan_%28genshin_impact%29+");
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

            System.out.println(openConnection.getContent());

            // Output Stream
            DataOutputStream dataOutStream
                    = new DataOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream("./resources/sample.html")));

            // Read Data
            byte[] b = new byte[4096];
            int readByte = 0;

            while (-1 != (readByte = dataInStream.read(b))) {
                dataOutStream.write(b, 0, readByte);
            }

            // Close Stream
            dataInStream.close();
            dataOutStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }


}
