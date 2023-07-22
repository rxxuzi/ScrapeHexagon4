package crawler;

public class Main {
    public static void main(String[] args) {
        Downloader downloader = new Downloader("https://hijiribe.donmai.us/posts/6483817?q=mostima_%28arknights%29");
        downloader.run();
    }
}
