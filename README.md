# ScrapeHexagon4

## Version

+ JDK 20.0.1
+ scala 2.12
+ Jsoup v1.15.4
+ Gson v2.10


コマンドラインで次のコマンドを実行してJdkがver20以上であることを確認してください

```shell
javac -version
java -version
```

## 改善点

Chromeの仮想環境 -> HTTPRequest に変更 

## 設定

[SETTING](config/SETTING.ini)を参照してください

NOT検索の設定は以下のファイルを書き換えてください
[NOT検索](config/EXCLUSION.txt)

## scala 

メインクラスは[ScalaMain.scala](src/scala/open/ScalaMain.scala)

[SCALA](src/scala)

## アーカイブ

アーカイブとして作成したフォルダをzip化できます。

zipのパスは[SETTING](config/SETTING.ini)を参照してください
