package crawler;

import data.WriteToJson;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class GetTagDriver {
    private static final int maxPage = 5000;
    private static final int minPosts = 200;
    private static int tagCounter = 0;

    public static void main(String[] args) {

        WriteToJson.file = "popularTag.json";

        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver","./opt/chromedriver_win32/chromedriver.exe");


        //インスタンス化
        WebDriver driver = new ChromeDriver();

        String baseUrl = "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=0&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=name";

        driver.get(baseUrl);

        System.out.println("Get All Tags");

        wait(driver,700);

        //重複しないように設定
        String nowUrl;

        //ページの切り替え
        for(int i = 0 ; i < maxPage ; i ++ ){

            nowUrl = driver.getCurrentUrl();

            List<WebElement> td = driver.findElements(By.tagName("td"));

            for (int j = 0 ; j < td.size() ; j += 2) {
                WebElement t = td.get(j);
                try{
                    WebElement posts = t.findElement(By.className("post-count"));
                    String postCount = posts.getAttribute("title");
                    int pci = Integer.parseInt(postCount);

                    if (pci < minPosts){
                        continue;
                    }

                    List<WebElement> tagwords = t.findElements(By.className("tag-type-0"));
                    for (WebElement tag : tagwords) {
                        String tagName = tag.getText();
                        if(Objects.equals(tagName, "?")){
                            continue;
                        }
                        WriteToJson.add(tagCounter, tagName ,pci ,0);
                        tagCounter++;
                    }
                }catch (Exception e){
                    System.out.println("No Tag");
                }

            }
            wait(driver,200);

            try {
                WebElement nextPageBtn = driver.findElement(By.className("paginator-next"));
                nextPageBtn.click();
            }catch (Exception e){
                System.out.println("No Next Page");
                break;
            }
            if(nowUrl.equals(driver.getCurrentUrl())){
                System.out.println("Same Page");
                break;
            }
            if(i != 0 && i % 50 == 0){
                WriteToJson.save();
            }
        }


        wait(driver, 700);
        //終了
        driver.quit();

        WriteToJson.save();

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
}