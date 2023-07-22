package data;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class PropertiesHandler {
    /**
     * 指定されたファイルを設定ファイルとして読み込み、 {@link Properties} にして返します。
     * 指定されたファイルの各行は「キー=値」となるプロパティ、または#で始まるコメントを含むことが出来ます。
     *
     * @param file 読み込むファイル
     * @return 読み込んだ設定
     * @throws IOException 読み込みに失敗した場合
     */
    public static Properties load(File file) throws IOException {
        return load(file.getPath());
    }

    /**
     * 指定されたファイルを設定ファイルとして読み込み、 {@link Properties} にして返します。
     * 指定されたファイルの各行は「キー=値」となるプロパティ、または#で始まるコメントを含むことが出来ます。
     *
     * @param filePath 読み込むファイルのパス
     * @return 読み込んだ設定
     * @throws IOException 読み込みに失敗した場合
     */
    public static Properties load(String filePath) throws IOException {
        Properties properties = new Properties();
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

    public static void main(String[] args) throws IOException {
        Properties props = PropertiesHandler.load("./config/SETTING.ini");
//        System.out.println(props);

        var k = props.keySet();
        System.out.println(k);
        for(var key : k){
            System.out.print(key.toString() + " : ");
            System.out.println(props.get(key));
        }
    }
}
