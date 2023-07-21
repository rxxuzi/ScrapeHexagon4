package main;

import java.io.File;
public class DeletePics {
    DeletePics(){
        File[] dir = new File("./rsc/pics").listFiles();
        assert dir != null;
        for (File file : dir) {
            if (file.isFile()) {
                file.delete();
            }
        }
        if(dir.length == 0){
            System.out.println("Pics deleted");
        }
    }
}
