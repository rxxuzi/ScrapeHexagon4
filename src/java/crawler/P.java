package crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class P {

    public static void main(String[] args) {
        try {
            String url = "https://hijiribe.donmai.us/posts/6483817?q=mostima_%28arknights%29";
            Document document = Jsoup.connect(url).get();

            Element imageElement = document.selectFirst("#image img");
            if (imageElement != null) {
                String imageUrl = imageElement.attr("src");
                System.out.println("Image URL: " + imageUrl);

                // You can download the image using the downloadImage method here.
            } else {
                System.out.println("Image not found.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

