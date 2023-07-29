package global;

import data.Archive;

import java.io.IOException;

public class A {
    public static void main(String[] args) {
        String path = GlobalProperties.PIC_DIR;
        Archive archive = new Archive(path);
        try {
            archive.createZip();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
