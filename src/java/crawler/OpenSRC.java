package crawler;

import fast.Tag;
import global.GlobalProperties;
import global.OpenHTML;
import global.Status;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
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
public final class OpenSRC  {

    public final static String http = GlobalProperties.DOMAIN;
    public final static String BASIC_URL = GlobalProperties.DOMAIN+"posts?page=";
    public final static int MAX_IMG_CNT = GlobalProperties.MAX_IMG_CNT;
    public static int PAGE_COUNT = 1;
    private static final String BASIC_TAG = "&tags=";
    public static final AtomicBoolean isRunning = new AtomicBoolean(true);
    private String TAG = "exusiai_%28arknights%29";
    private int px = 0;
    public static int imgCnt = 0;
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

            Document document = OpenHTML.html(url);
            System.out.println(document.title());

            //get #posts img element
            Elements posts = document.getElementsByClass("post-preview-link");

            Downloader[] d;


            boolean fullLoop =  imgCnt + posts.size() < MAX_IMG_CNT;

            if(posts.size() == 0 ){
                isRunning.set(false);
                return;
            }

            if(fullLoop){
                d = new Downloader[posts.size()];

                System.out.println("fullLoop : " + fullLoop + ", count : " + imgCnt + ", posts size : " + posts.size() + " MAX_IMG_CNT : " + MAX_IMG_CNT);
                // 並列処理for文
                for (int i = 0; i < d.length; i++) {
                    var p = posts.get(i).attr("href");
                    String link = http + p ;
                    if (imgCnt < MAX_IMG_CNT ) {
                        d[i] = (new Downloader(link));
                        d[i].start();
                    }
                }
                imgCnt += posts.size();
            }else {
                int n =  MAX_IMG_CNT - imgCnt;
                System.out.println("fullLoop : " + fullLoop + ", count : " + imgCnt + ", posts size :  " + n + " MAX_IMG_CNT : " + MAX_IMG_CNT);

                d = new Downloader[n];

                System.out.println("Not max saves!");
                // 並列処理for文
                for(int i = 0 ; i < d.length && i < posts.size()  ; i ++ ){
                    var p = posts.get(i).attr("href");
                    String link = http + p ;
                    if (imgCnt < MAX_IMG_CNT ) {
                        d[i] = new Downloader(link);
                        d[i].start();
                    }
                }
                imgCnt += n ;
            }



            if(imgCnt > MAX_IMG_CNT){
                isRunning.set(false);
                System.out.println("finish");
                Status.setStatusCode(0);
                Thread.sleep(1000);
            }


            System.out.println("images from : " + url);
            System.out.println(imgCnt + " images downloaded");

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.out.println("InterruptedException");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
