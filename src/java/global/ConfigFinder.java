package global;

import java.io.File;

public class ConfigFinder {

    public static String findSettingIni(String directoryPath) {
        File directory = new File(directoryPath);

        // ディレクトリが存在しない場合はnullを返す
        if (!directory.exists() || !directory.isDirectory()) {
            return null;
        }

        // ディレクトリ内のファイルとフォルダーを取得
        File[] files = directory.listFiles();

        // ディレクトリ内のファイルとフォルダーをチェック
        for (File file : files) {
            if (file.isDirectory()) {
                // 再帰的にディレクトリを探索
                String result = findSettingIni(file.getAbsolutePath());
                if (result != null) {
                    return result; // 見つかった場合はそのパスを返す
                }
            } else if (file.isFile() && file.getName().equals("SETTING.ini")) {
                return file.getAbsolutePath(); // "SETTING.ini"が見つかった場合はパスを返す
            }
        }

        return null; // 見つからなかった場合はnullを返す
    }

    public static void main(String[] args) {
        String startDirectory = "./run/Run-ui/output/json"; // 探索を開始するディレクトリのパスを指定

        // ディレクトリ探索を実行
        String settingIniPath = findSettingIni(startDirectory);

        if (settingIniPath != null) {
            System.out.println("SETTING.ini found at: " + settingIniPath);
        } else {
            System.out.println("SETTING.ini not found in the specified directory and its subdirectories.");
        }
    }
}
