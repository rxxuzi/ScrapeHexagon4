package crawler;

import global.GlobalProperties;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static int IMG = 10;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of images you want to download: ");
        try{
            IMG = sc.nextInt();
        }catch (InputMismatchException e ){
            System.out.println("Wrong input");
            System.exit(1);
        }

        GlobalProperties properties = new GlobalProperties();

        OpenSRC opensrc = new OpenSRC();
        long  startTime = System.currentTimeMillis();
        System.out.println("Enter the Word");
        opensrc.setTag(sc.nextLine());
        opensrc.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}
