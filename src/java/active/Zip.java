package active;

import data.Archive;

import java.io.IOException;

public class Zip {
    public static void main(String[] args) throws IOException {
        Archive a = new Archive();
        a.createZip();
    }
}
