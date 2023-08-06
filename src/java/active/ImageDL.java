package active;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageDL {
    public static void main(String[] args) {
        String path ="https://cdn.donmai.us/original/9a/ef/__vill_v_honkai_and_1_more_drawn_by_grape_pixiv27523889__9aef8071293ffbe721206a65fe03fcc3.jpg";
        String fileName = path.substring(path.lastIndexOf("/") + 1);

        // remove fileformat extension
        fileName = fileName.substring(0, fileName.lastIndexOf("."));

        fileName = fileName + ".jpg";

        try {
            downloadImage(path,fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        connection.disconnect();
    }
}
