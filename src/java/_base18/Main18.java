package _base18;

import crawler.OpenHTML;
import global.GlobalProperties;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main18 {
    public static void main(String[] args) throws InterruptedException {
        // STOPSHIP: 2023/08/14
        Scanner scanner  = new Scanner(System.in);
        System.out.println("Enter the tag: ");
        String tag = scanner.nextLine();
        String v_url = "https://momon-ga.com/tag/" + tag + "/page/";
        for (int i = 1 ; i < 5; i ++ ){
            String url = v_url + i;
            OpenHTML openHTML = new OpenHTML();
            Document doc = openHTML.html(url);

            Elements posts = doc.getElementsByClass("post-list");
            Elements pages = Objects.requireNonNull(posts.first()).getElementsByTag("a");
            List<Downloader18> d18s = new ArrayList<>();
            System.out.println(pages.size());
            for (Element page : pages) {
                String title = page.getElementsByTag("span").text();
                File dir = new File(GlobalProperties.BASE_DIR+title);
                if(dir.exists()){
                    System.out.println("Skip");
                    continue;
                }
                String link = page.select("a").attr("abs:href");
                d18s.add(new Downloader18(link));

            }
            for (Downloader18 d18 : d18s) {
                d18.start();
            }
            for (Downloader18 d18 : d18s) {
                try {
                    d18.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }



    }
}

