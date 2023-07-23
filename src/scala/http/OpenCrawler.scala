package http

import org.jsoup.Jsoup

import java.io.FileOutputStream
import java.net.URL
import scala.io.Source

object OpenCrawler {
  def main(args: Array[String]): Unit = {
    val url = new URL("https://danbooru.donmai.us/posts/6519591?q=blonde_hair")
    val html = Source.fromInputStream(url.openStream()).mkString
    val document = Jsoup.parse(html)
    val element = document.getElementById("image")
    val imageUrl = element.attr("src")
    val filepath = "./resources/scala.jpg"
    downloadImage(imageUrl, filepath)
  }

  def downloadImage(fileStreamURL : String,saveAsFile: String): Unit = {
    val url = new URL(fileStreamURL)
    val in = url.openStream()
    val fos = new FileOutputStream(saveAsFile)
    try {
      val buf = new Array[Byte](1024)
      var n = in.read(buf)
      while (n != -1) {
        fos.write(buf, 0, n)
        n = in.read(buf)
      }
    } finally {
      fos.close()
      in.close()
    }
  }
}

