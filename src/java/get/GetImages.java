package get;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.Random;

public class GetImages {
    static Random random = new Random();
    private static int imgCount = 0;
    private static final String web_URL = "";
    public static void main(String[] args) {
        try {
            URL url = new URL(web_URL);
            URLConnection con = url.openConnection();
            InputStream stream = con.getInputStream();
            Document doc = Jsoup.parse(stream, "UTF-8", web_URL);
            Elements imgs = doc.getElementsByTag("img");
            var count = 0;

            for (Element img : imgs) {
                count ++;
                imgCount++;
                if(count > 20) break;
                getImageByElements(img);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static void getImageByElements(Element Img) throws IOException {
        String imageUrl = Img.id() + random.nextInt(100);
        saveImage(imageUrl);
    }
    // 画像を保存するメソッド
    private static void saveImage(String imageUrl) throws IOException {
        // URL接続を開く
        URL url = new URL(imageUrl);
        //URLからread
        BufferedImage image = ImageIO.read(url);
        if(image != null){
            //ファイル指定
            File outPutFile = new File("./rsc/pics"+ imageUrl + ".png");
            //ファイルに保存
            ImageIO.write(image, "png", outPutFile);

            System.out.println("("+imgCount + ") SUCCESS -> " + outPutFile.getPath().toUpperCase());
        }else{
            System.out.println("("+imgCount+ ") FAIL -> " + imageUrl.toUpperCase());
        }
    }
}
