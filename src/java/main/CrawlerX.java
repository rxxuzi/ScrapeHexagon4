package main;

import crawler.OpenSRC;
import data.CheckImage;
import global.GlobalProperties;

public class CrawlerX extends Thread{
    public String word;
    public int max;

    public CrawlerX(String SeachWord , int MaxImageCount){
        this.word = SeachWord;
        this.max= MaxImageCount;
    }

    public void run(){
        GlobalProperties.Compare(max);
        long  startTime = System.currentTimeMillis();
        OpenSRC opensrc = new OpenSRC();
        opensrc.setTag(word);
        opensrc.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        CheckImage ch = new CheckImage();
    }
}
