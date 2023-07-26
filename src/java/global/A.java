package global;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class A {
    public static void main(String[] args) {
        Scanner sc1 = new Scanner(System.in);
        System.out.print("Enter the number of images you want to download: ");
        int c = 0;
        try{
            c = sc1.nextInt();
        }catch ( Exception e ){
            System.out.println("Wrong input");
            System.exit(1);
        }
        System.out.println("Enter the tag of the image you want to download: ");
        Scanner sc2 = new Scanner(System.in);
        String tag = sc2.nextLine();

        System.out.println(c + ": " + tag);

        List<String> list1 = new LinkedList<>();
        List<String> list2 = new LinkedList<>();
        var E = new HashMap<String , List>();

        for(int i = 0; i < 10; i++){
            list1.add(String.valueOf(i));
        }

        for(int i = 0; i < 10; i++){
            list2.add(String.valueOf(i*2));
        }
        E.put("list1" , list1);
        E.put("list2" , list2);

        System.out.println(list1);
        System.out.println(list2);
        System.out.println(E);

        File file = new File("./resources/test.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(gson.toJson(E));
            fw.close();
            System.out.println("Write to file successfully / Size Of List" + E.size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
