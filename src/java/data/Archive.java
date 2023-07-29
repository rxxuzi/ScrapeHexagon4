package data;

import global.GlobalProperties;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ファイル名の指定
 * <pre>
 *     {@code
 *     Archive archive = new Archive("./output/pics");
 *     archive.createZip();}
 * </pre>
 *
 * Pictureをzipに保存するクラス
 * @author rxxuzi
 * @version 1.0
 * @since 1.0
 * @see java.io.File
 */
public class Archive {
    private final String ZIP_FILE_NAME;
    private final String LOG_FILE_NAME = "./archive/log.txt";
    private final static String THIS_DIR = "./archive/";
    File dir = new File("./output/pics");
    File[] files = dir.listFiles();
    private String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private String time = new SimpleDateFormat("HH:mm:ss").format(new Date());

    public Archive(String dir){
        this.dir = new File(dir);
        this.files = this.dir.listFiles();
        ZIP_FILE_NAME = THIS_DIR + (dirLength(THIS_DIR)) + "-pic"+ ".zip";
    }

    public Archive(){
        this.dir = new File(GlobalProperties.PIC_DIR);
        this.files = this.dir.listFiles();
        ZIP_FILE_NAME = THIS_DIR + (dirLength(THIS_DIR)) + "-pic"+ ".zip";
    }
    /**
     * zipファイルに保存する
     * @throws IOException ファイル関連のエラー
     */
    public void createZip() throws IOException {
        FileOutputStream fos = new FileOutputStream(ZIP_FILE_NAME);
        ZipOutputStream zos = new ZipOutputStream(fos);
        zos.setLevel(Deflater.BEST_COMPRESSION); // 圧縮レベルを最大に設定

        for (File file : files) {
            zos.putNextEntry(new ZipEntry(file.getName()));
            Files.copy(Paths.get(file.getAbsolutePath()), zos);
            zos.closeEntry();
        }
        zos.close();
        fos.close();
    }

    public void log() throws IOException {
        String path = THIS_DIR + date + time + ".log";
        File log = new File(path);
        FileWriter fw = new FileWriter(log, true);
        fw.write(date + " " + time + "\n");
        fw.close();
    }

    public static int dirLength(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files != null) {
            return files.length;
        }else {
            return 0;
        }
    }



}
