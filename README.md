# ScrapeHexagon4

![Static Badge](https://img.shields.io/badge/license-MIT-blue)
![Static Badge](https://img.shields.io/badge/java-20-blue)
![Static Badge](https://img.shields.io/badge/author-rxx-green)


<img alt="gui" src="docs/gui.png" width=1097>

[//]: # (<img alt="web-ui" src="docs/web-ui.png" width="1414">/)

## Version

+ JDK 20.0.1
+ scala 2.12
+ Jsoup v1.15.4
+ Gson v2.10

Make sure that the jdk is ver20 or higher by executing the following command on the command line
```shell
javac -version
java -version
```
## COPYRIGHT

Copyright (c) 2023 Rxxuzi. See [LICENSE](LICENCE) for details.

## GETTING START

It can be executed by specifying the `****` part of the following command

**From command line**: `run/Run-CommandLine/RunByCommandLine.jar`

**From ui**: `run/Run-ui/ScrapeHexagon4.jar`.

~~~shell
git clone https://github.com/rxxuzi/ScrapeHexagon4
cd ScrapeHexagon4/run/
java -jar ****
~~~

Or build the entire project and run [Main.java](src/java/main/Main.java)

Even if sbt is not installed, it can be executed with java alone.

## SETTING

Refer to [SETTING](config/SETTING.ini) for file format specification, path specification, etc.

To configure NOT search settings, rewrite the following file
[NOT search](config/EXCLUSION.txt)

## Web UI

**wip**  [web-ui](web)

## Archive

You can zip a folder created as an archive.

See [SETTING](config/SETTING.ini) for the zip path
