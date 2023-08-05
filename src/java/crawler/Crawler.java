package crawler;

import data.Archive;
import data.CheckImage;
import error.OpenHTMLException;
import fast.Del;
import fast.WordMatcher;
import global.GlobalProperties;
import global.Status;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Crawler {
    public static void main(String[] args) {
        Archive.delete(GlobalProperties.PIC_DIR);
        Archive.delete(GlobalProperties.JSON_DIR);

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of images you want to download: ");
        GlobalProperties properties = new GlobalProperties();
        properties.makeDir();

        int x = -1;
        try{
            x = sc.nextInt();
            GlobalProperties.Compare(x);
        }catch (InputMismatchException e ){
            System.out.println("Wrong input");
            System.exit(1);
        }


        OpenSRC opensrc = new OpenSRC();
        System.out.println("Enter the Word");
        String word = sc.next();

        long  startTime = System.currentTimeMillis();
        opensrc.setTag(word);
        opensrc.run();

        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        CheckImage ch = new CheckImage();
        if (ch.dirLength() >= x){
            System.out.println("Download complete");
            Archive  ar = new Archive();
            try {
                ar.createZip();
                System.out.println("Save as Zip!!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (ch.dirLength() < x){
            System.out.println("Download Failed" + "\n" + "Status Code : 100");
            Status.setStatusCode(100);
        }


    }
}
