package crawler;

import global.GlobalProperties;
import open.SRC;

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
        System.out.print("Enter the tag you want to download: ");
        String s = sc.nextLine();

        try{
            GlobalProperties properties = new GlobalProperties();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        open.SRC yelan = new SRC();
        long  startTime = System.currentTimeMillis();
        yelan.setTag(s);
        yelan.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}
