#!/bin/bash
cd ./src # srcフォルダに移動
javac -d ../bin java/**/*.java # srcフォルダ内のjavaフォルダ以下の全てのjavaファイルをコンパイルして、binフォルダに出力
cd ../bin # binフォルダに移動
java java.main.Main # java.main.Mainを実行
