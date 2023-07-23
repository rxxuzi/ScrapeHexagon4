package crawler;

import global.GlobalProperties;

public class Main {
    public static int IMG = 10000;
    public static void main(String[] args) {
        String s = "trer";
        boolean bool = Boolean.parseBoolean(s);
        if(bool){
            System.out.println("true");
        }
        try{
            GlobalProperties properties = new GlobalProperties();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        Yelan  yelan = new Yelan();
        long  startTime = System.currentTimeMillis();
        yelan.run();
        long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
    }
}
