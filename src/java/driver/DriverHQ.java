
package driver;

import fast.FileName;
import fast.Log;
import fast.Tag;
import io.github.bonigarcia.wdm.WebDriverManager;
import jdk.jfr.BooleanFlag;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DriverHQ {
    private static final String Path = "./resources/pic/";
    private static final int maxPage = 100;
    public static int maxImage = 10;
    private static int imageCounter = 0;

    @BooleanFlag
    private static boolean blockNSFW  = false;
    @BooleanFlag
    private static boolean isSuccess = false;
    @BooleanFlag
    private static boolean canSaveVideo = false;
    @BooleanFlag
    private static boolean isVideo = false;

    @BooleanFlag
    private static final boolean hq = false;

    private static int totalImage = 0;
    public static String tag = "";
    public static String url ;


    public static void run() {

        imageCounter = 0;
        isSuccess = false;

        System.out.println(Tag.translate(tag));

        url = "https://betabooru.donmai.us/posts?tags=" + Tag.translate(tag) + "&z=1";

        WebDriverManager.chromedriver().setup();

        //optフォルダーに入れたdriverのpath
        System.setProperty("webdriver.chrome.driver","./opt/chromedriver_win32/chromedriver.exe");

        //インスタンス化
        ChromeOptions options = new ChromeOptions();



        //非表示
//        options.addArguments("--headless");
//        options.addArguments("--start-minimized");
        WebDriver driver = new ChromeDriver(options);

        // JavaScriptを実行して、ウィンドウを背面に設定する
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.blur();");

        // ウィンドウをスクリーンの右下に移動する
        Dimension windowSize = driver.manage().window().getSize();


        driver.manage().window().setPosition(new Point(windowSize.getWidth() -1 , windowSize.getHeight() -1));


        //ウィンドウのサイズを最小化する
        driver.manage().window().setSize(new Dimension(0, 0));
//        driver.manage().window().maximize();
        wait(driver,1500);


        driver.get(url);

//        System.out.println(driver.getTitle());

        wait(driver,700);

        //重複しないように設定
        String nowUrl;

        //ページの切り替え
        for(int i = 0 ; i < maxPage ; i ++ ){
            if(imageCounter >= maxImage){
                break;
            }

            nowUrl = driver.getCurrentUrl();

            //imgタグのついているものをListにまとめる
            List<WebElement> imageLinks = driver.findElements(By.className("post-preview-link"));
//            System.out.println("Link Size" + imageLinks.size());

            //それぞれのimageUrlに対してスクレイピング
            for (WebElement link : imageLinks) {

                if(imageCounter >= maxImage){
                    break;
                }

                //プレビュー画像に付いていたリンクを抜き取る
                String linkUrl = link.getAttribute("href");

                if (linkUrl != null && linkUrl.startsWith("http")) {
                    //リンク先へ移動
                    driver.navigate().to(linkUrl);
                    wait(driver,700);

                    if(hq){
                        //オリジナルバージョンがあった場合クリックする
                        if(driver.findElement(By.className("image-view-original-link")) != null){
                            WebElement viewOriginal = driver.findElement(By.className("image-view-original-link"));
                            viewOriginal.click();
                        }
                    }

                    //レーティングをチェック

                    WebElement preview = driver.findElement(By.id("post-info-rating"));
                    String rating = preview.getText().split(":")[1].trim();

                    System.out.print(rating+", ");

                    WebElement image = driver.findElement(By.id("image"));

                    //src属性からurlを取得
                    String imageUrl = image.getAttribute("src");

                    System.out.println(imageUrl);

                    //拡張子をチェック
                    checkExt(imageUrl);

                    //blockNSFW
                    if(rating.equals("Explicit") && blockNSFW){
                        System.out.println("Blocked");
                        Log.write(totalImage + FileName.translate(tag)  + ext(imageUrl) + " ("  + rating  +") : " + Log.getTime() + " : Blocked");
                    }else{
                        if(canSaveVideo){
                            String fileName = totalImage + FileName.translate(tag) + ext(imageUrl);
                            if(isVideo){
                                saveVideo(imageUrl, fileName);
                                Log.write(fileName + " ("  + rating  +") : " + Log.getTime() + " : " + linkUrl);
                                totalImage ++;
                                System.out.println("Video Download");
                            }else{
                                saveImage(imageUrl,fileName);
                                Log.write(fileName + " ("  + rating  +") : " + Log.getTime() + " : " + linkUrl);
                                totalImage ++;
                            }
                        }else {
                            if(!isVideo){
                                String fileName = totalImage + FileName.translate(tag) + ext(imageUrl);
                                Log.write(fileName + " ("  + rating  +") : " + Log.getTime() + " : " + linkUrl);
                                saveImage(imageUrl,fileName);
                                totalImage ++;
                            }
                        }
                    }

                    //前のページに戻る
                    driver.navigate().back();

                    //ドライバーの待ち時間
                    wait(driver,30);

                }
            }

            wait(driver,250);

            try {
                WebElement nextPageBtn = driver.findElement(By.className("paginator-next"));
                nextPageBtn.click();
            }catch (Exception e){
                System.out.println("No Next Page");
                Log.error("No Next Page");
                break;
            }

            if(nowUrl.equals(driver.getCurrentUrl())){
                System.out.println("Same Page");
                break;
            }
        }


        wait(driver, 700);
        //終了
        driver.quit();

        isSuccess = maxImage == imageCounter;

        System.out.println("MaX Picture -> " + maxImage);
        System.out.println("GeT Picture -> " + imageCounter);

    }

    private static void checkExt(String ex){
        if(ex.endsWith("gif")){
            isVideo = false;
        } else if(ex.endsWith("webm")){
            isVideo = true;
        } else if(ex.endsWith("mp4")){
            isVideo = true;
        } else if (ex.endsWith("mov")){
            isVideo = true;
        } else isVideo = ex.endsWith("wmv");
    }
    private static String ext(String ex){
        String extension;
        if(ex.endsWith("gif")){
            extension = ".gif";
        } else if(ex.endsWith("webm")){
            extension = ".webm";
        } else if(ex.endsWith("mp4")){
            extension = ".mp4";
        } else if (ex.endsWith("mov")){
            extension = ".mov";
        } else if (ex.endsWith("wmv")){
            extension = ".wmv";
        }else {
            extension = ".png";
        }
        return extension;
    }

    /**
     * 画像を保存するメソッド
     * 保存先のディレクトリ指定はこのclassのstatic変数Pathで行う
     * @param imgURL スクレイプするURL
     * @param fileName 保存するファイル名
     */
    private static void saveImage(String imgURL , String fileName){
        try{
            URL url = new URL(imgURL);
            InputStream inputStream = url.openStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            FileOutputStream fos = new FileOutputStream(Path + fileName);
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
            }
            Thread.sleep(5);

            in.close();
            fos.close();
        }catch (IOException e){
            Log.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        imageCounter ++;
    }


    /**
     * 動画を保存するメソッド
     * 保存先のディレクトリ指定はこのclassのstatic変数Pathで行う
     * @param imgURL スクレイプするURL
     * @param fileName 保存するファイル名
     */
    private static void saveVideo(String imgURL , String fileName){
        try{
            URL url = new URL(imgURL);
            InputStream inputStream = url.openStream();
            BufferedInputStream in = new BufferedInputStream(inputStream);
            FileOutputStream fos = new FileOutputStream(Path + fileName);
            byte[] dataBuffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fos.write(dataBuffer, 0, bytesRead);
            }
            Thread.sleep(5);

            in.close();
            fos.close();
        }catch (IOException e){
            Log.error(e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        imageCounter ++;
    }

    /**
     *
     * @param webDriver WebDriverからの変数
     * @param time　ブラウザ操作の待ち時間
     */
    private static void wait(WebDriver webDriver , long time) {
        Duration duration = Duration.ofMillis(time);
        webDriver.manage().timeouts().implicitlyWait(duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static boolean getIsSuccess() {
        return isSuccess;
    }

    public static void blockExplicit(boolean blockNSFW) {
        DriverHQ.blockNSFW = blockNSFW;
    }

    public static void search(String tag){
        DriverHQ.tag = tag;
    }

    public static void setCanSaveVideo(boolean canSaveVideo) {
        DriverHQ.canSaveVideo = canSaveVideo;
    }

    public static void setTotalImage(int totalImage) {
        DriverHQ.totalImage = totalImage;
    }
}