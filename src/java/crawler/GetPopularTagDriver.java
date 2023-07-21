package crawler;

import data.WriteToJson;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GetPopularTagDriver {

    private static final int maxPage = 1200;

    private static int tagCounter = 0;
    /**
     * jsonファイルをセーブするまでのインターバル
     */
    private static final int saveInterval = 20;

    /**
     * General -> type - 0
     * Artist -> type - 1
     * Copyright -> type - 3
     * Character -> type - 4
     * Meta -> type - 5
     */
    private static int categoryType = 1;

    private static final int minPosts = switch (categoryType){
        case 0 -> 1000;
        case 1 -> 30;
        case 3 -> 100;
        case 4 -> 50;
        default -> 200;
    };

    public static void main(String[] args) {
        do{
            Scanner scanner = new Scanner(System.in);
            System.out.println("categoryType : ");
            categoryType = scanner.nextInt();
        }while (categoryType < 0 || categoryType > 5);

        WriteToJson.file = switch (categoryType){
            case 0 -> "GeneralPTag.json";
            case 1 -> "ArtistTag.json";
            case 3 -> "CopyrightTag.json";
            case 4 -> "CharacterTag.json";
            case 5 -> "MetaTag.json";
            default -> "Else.json";
        };

        WebDriverManager.chromedriver().setup();
        System.setProperty("webdriver.chrome.driver","./opt/chromedriver_win32/chromedriver.exe");


        //インスタンス化
        WebDriver driver = new ChromeDriver();
        String baseUrl = switch (categoryType){
            case 0 -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=0&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
            case 1 -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=1&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
            case 3 -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=3&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
            case 4 -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=4&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
            case 5 -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bcategory%5D=5&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
            default -> "https://danbooru.donmai.us/tags?commit=Search&search%5Bhas_wiki_page%5D=yes&search%5Bhide_empty%5D=yes&search%5Bis_deprecated%5D=no&search%5Border%5D=count";
        };

        driver.get(baseUrl);

        System.out.println("Get All Tags");

        wait(driver,700);

        //重複しないように設定
        String nowUrl;

        boolean isExist = false;

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
                        isExist = true;
                        break;
                    }

                    List<WebElement> tagwords = switch (categoryType){
                        case 0 -> t.findElements(By.className("tag-type-0"));
                        case 1 -> t.findElements(By.className("tag-type-1"));
                        case 3 -> t.findElements(By.className("tag-type-3"));
                        case 4 -> t.findElements(By.className("tag-type-4"));
                        default -> t.findElements(By.className("tag-type-5"));
                    };
                    for (WebElement tag : tagwords) {
                        String tagName = tag.getText();
                        if(Objects.equals(tagName, "?")){
                            continue;
                        }
//                        System.out.println(tagCounter + tagName);
                        WriteToJson.add(tagCounter, tagName ,pci ,categoryType);
//                        System.out.println(tagCounter + ":" + tagName);
                        tagCounter++;
                    }
                }catch (Exception e){
                    System.out.println("No Tag");
                }

            }
            wait(driver,100);

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
            if(i != 0 && i % saveInterval == 0){
                WriteToJson.save();
            }
            if(isExist){
                break;
            }
        }


        wait(driver, 700);
        //終了
//        driver.quit();

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