package crawler;

import data.CheckImage;
import fast.Del;
import global.GlobalProperties;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Crawler {
    public static void main(String[] args) {
        Del.allPicDelete();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of images you want to download: ");
        GlobalProperties properties = new GlobalProperties();

        try{
            int x = sc.nextInt();
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
        ch.check();
    }
}
