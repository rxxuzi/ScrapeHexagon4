package global;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;

public class GlobalProperties {
    /**
     * config内のconfig/SETTING.iniを読み込む
     * @throws IOException - 読み込み失敗
     */
    public static boolean CUSTOM = false;
    public static boolean MAKE_BIN ;
    public static boolean TAG2JSON = false;
    public static boolean NSFW = false;
    public static String PIC_DIR = "./output/pics/";
    public static String JSON_DIR = "./output/json/";
    public static String FILE_FORMAT = ".png";
    public static boolean DEBUG = false;
    public static String DOMAIN ;
    public static int MAX_IMG_CNT = 100;

    public GlobalProperties() {

        try{

            Properties props = GlobalProperties.load("./config/SETTING.ini");
            var k = props.keySet();
//            System.out.println(k);
//            System.out.println(props.get("CustomSetting").toString() + "cs");
            CUSTOM = Boolean.parseBoolean(props.get("CustomSetting").toString());
            if(CUSTOM){
                MAKE_BIN = Boolean.parseBoolean(props.get("4Bin").toString());
                TAG2JSON = Boolean.parseBoolean(props.get("Tag2Json").toString());
                NSFW = Boolean.parseBoolean(props.get("NSFW").toString());
                PIC_DIR = props.get("PicDir").toString();
                JSON_DIR = props.get("JsonDir").toString();
                FILE_FORMAT = props.get("FileFormat").toString();
                MAX_IMG_CNT = Integer.parseInt(props.get("MaxPic").toString());
            }
        }catch (IOException | NullPointerException e){
            e.printStackTrace();
        }

        File f;
        f = new File(PIC_DIR);
        if(!f.exists()){
            f.mkdirs();
        }
        f  = new File(JSON_DIR);
        if(!f.exists()){
            f.mkdirs();
        }

        if(NSFW){
            DOMAIN = "https://danbooru.donmai.us/";
        }else{
            DOMAIN = "https://safebooru.donmai.us/";
        }
    }

    /**
     * 指定されたファイルを設定ファイルとして読み込み、 {@link java.util.Properties} にして返します。
     * 指定されたファイルの各行は「キー=値」となるプロパティ、または#で始まるコメントを含むことが出来ます。
     *
     * @param file 読み込むファイル
     * @return 読み込んだ設定
     * @throws IOException 読み込みに失敗した場合
     */
    public static java.util.Properties load(File file) throws IOException {
        return load(file.getPath());
    }

    /**
     * 指定されたファイルを設定ファイルとして読み込み、 {@link java.util.Properties} にして返します。
     * 指定されたファイルの各行は「キー=値」となるプロパティ、または#で始まるコメントを含むことが出来ます。
     *
     * @param filePath 読み込むファイルのパス
     * @return 読み込んだ設定
     * @throws IOException 読み込みに失敗した場合
     */
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