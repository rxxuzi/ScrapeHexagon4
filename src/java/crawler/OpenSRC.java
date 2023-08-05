package crawler;

import error.OpenHTMLException;
import fast.Tag;
import global.GlobalProperties;
import global.Status;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * This class is a crawler of the OpenSRC website.
 * It can be used to download images from the website.
 *
 * <p>The following example illustrates how to use this class:
 *
 * <pre>
 *     {@code
 *       OpenSRC open = new OpenSRC(); //create a new crawler
 *       open.setTag(exampleSearchedTag); //set tag
 *       open.run();//run the crawler
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
    public static int imgCnt = 0;
    public String getTag() {
        return TAG;
    }

    public void setTag(String TAGNAME) {
        this.TAG = Tag.translate(TAGNAME);
        System.out.println("setTag : " + TAG);
    }

    public void run()  {
        System.out.println("HEllo ! This method is run() from OpenSRC");
        while (isRunning.get()){
            int px = 0;
            System.out.println("run : " + isRunning.get() + ", count : " + imgCnt + ", px : " + px);
            sendPage(BASIC_URL + PAGE_COUNT + BASIC_TAG  + TAG);
            PAGE_COUNT++;
        }
        System.out.println("Last finish");
    }

    public void sendPage(String page){
        if(isRunning.get()){
            try{

                URL url = new URL(page);

                System.out.println("images from : " + url);

                Document document = OpenHTML.html(url);
                System.out.println(document.title());

                //get #posts img element
                Elements posts = document.getElementsByClass("post-preview-link");

                Downloader[] d;


                boolean fullLoop =  imgCnt + posts.size() < MAX_IMG_CNT;

                if(posts.isEmpty()){
                    isRunning.set(false);
                    return;
                }

                Downloader.skipCnt.set(0);

                if(fullLoop){
                    d = new Downloader[posts.size()];

                    System.out.println("fullLoop : " + true + ", count : " + imgCnt + ", posts size : " + posts.size() + " MAX_IMG_CNT : " + MAX_IMG_CNT);
                    // 並列処理for文
                    for (int i = 0; i < d.length; i++) {
                        var p = posts.get(i).attr("href");
                        String link = http + p ;
                        if (imgCnt < MAX_IMG_CNT ) {
                            d[i] = (new Downloader(link));
                            d[i].start();
                        }
                    }
                    for(Downloader dr : d){
                        try {
                            dr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    imgCnt += posts.size();
                    imgCnt -= Downloader.skipCnt.get();
                }else {
                    int n =  MAX_IMG_CNT - imgCnt;
                    System.out.println("fullLoop : " + false + ", count : " + imgCnt + ", posts size :  " + n + " MAX_IMG_CNT : " + MAX_IMG_CNT);

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

                    for(Downloader dr : d){
                        try {
                            dr.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    imgCnt += n ;
                    imgCnt -= Downloader.skipCnt.get();
                }

                if(imgCnt >= MAX_IMG_CNT){
                    isRunning.set(false);
                    System.out.println("img> max -> finish");
                    Status.setStatusCode(0);
                    Thread.sleep(1000);
                }
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
        }else{
            System.out.println("isRunning = " + isRunning);
            System.out.println("This is End");
        }

    }
}
