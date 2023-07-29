package crawler;

import data.Archive;
import data.CheckImage;
import fast.Del;
import global.GlobalProperties;
import global.Status;
import latest.OpenSRC;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Crawler {
    public static void main(String[] args) {
        Del.allPicDelete();
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


        latest.OpenSRC opensrc = new OpenSRC();
        System.out.println("Enter the Word");
        String word = sc.next();
        long  startTime = System.currentTimeMillis();
        opensrc.setTag(word);
        opensrc.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        CheckImage ch = new CheckImage();
        if (ch.dirLength() == x){
            System.out.println("Download complete");
            Archive  ar = new Archive();
            try {
                ar.createZip();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (ch.dirLength() < x){
            System.out.println("Download Failed" + "\n" + "Status Code : 100");
            Status.setStatusCode(100);
        }

    }
}
