package global;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.LinkedList;

public class NotSearch {
    public static LinkedList<String> Searched = new LinkedList<>();

    public NotSearch(){

    }
    public static java.util.Properties load(File file) throws IOException {
        return load(file.getPath());

    }

    public static java.util.Properties load(String filePath) throws IOException {
        java.util.Properties properties = new java.util.Properties();
        FileInputStream fis = null; // ファイルを適当に読み込む
        InputStreamReader isr = null; // 文字コードを直す
        try {
            fis = new FileInputStream(filePath);
            isr = new InputStreamReader(fis, Charset.defaultCharset());
            properties.load(isr);
        } finally { // 問題が起きたらとりあえずストリームを閉じ、残りは呼び出し側に任せる
            if (isr != null) {
                fis.close();
                isr.close();
            }
        }
        return properties;
    }
}
