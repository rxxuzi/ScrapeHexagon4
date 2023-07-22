package crawler;

import data.Properties;

public class Main {
    public static void main(String[] args) {
        try{
            Properties properties = new Properties();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
