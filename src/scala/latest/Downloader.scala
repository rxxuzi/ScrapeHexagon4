package latest

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import crawler.OpenSRC
import global.GlobalProperties
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.io._
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util
import scala.io.Source

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 * <pre>
 * {@code
 * Downloader downloader = new Downloader(srcURL);
 * downloader.run(); //run the crawler
 * }
 * </pre>
 *
 * Code snippet from OpenSRC.java
 *
 */
object Downloader {
  private val fileDir = GlobalProperties.PIC_DIR
  private val ext = GlobalProperties.FILE_FORMAT

  //image downloader
  @throws[IOException]
  private def downloadImage(imageUrl: String, fileName: String): Unit = {
    val url = new URL(imageUrl)
    val in = url.openStream()
    val fos = new FileOutputStream(fileName)
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

  // set fileFormat
  private def fileFormat(ex: String) = if (ex.endsWith("gif")) ".gif"
  else if (ex.endsWith("webm")) ".webm"
  else if (ex.endsWith("mp4")) ".mp4"
  else if (ex.endsWith("mov")) ".mov"
  else if (ex.endsWith("wmv")) ".wmv"
  else ext
}

final class Downloader(srcURL: String) extends Thread {
  try this.url = new URL(srcURL)
  catch {
    case e: MalformedURLException =>
      e.printStackTrace()
  }
  private var saveTag = GlobalProperties.TAG2JSON
  private var url: URL = null
  private var fileName: String = null

  override def run(): Unit = {
    if (latest.OpenSRC.isRunning.get) try {
      val html = Source.fromInputStream(url.openStream()).mkString
      val document = Jsoup.parse(html)
      //get #image img element
      val imageElement = document.getElementById("image")
      if (imageElement != null) {
        val imageUrl = imageElement.attr("src")
        //                System.out.println("Image URL: " + imageUrl);
        fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
        // remove fileformat extension
        fileName = fileName.substring(0, fileName.lastIndexOf("."))
        val fileFormat = Downloader.fileFormat(imageUrl)
        val filepath = Downloader.fileDir + fileName + fileFormat
        // 画像をダウンロードして保存する
        Downloader.downloadImage(imageUrl, filepath)
        System.out.println("Download image : " + url)
      }
      else System.out.println("Image not found.")
    } catch {
      case e: IOException =>
        e.printStackTrace()
    }
  }

  final private val generalTagList = new util.ArrayList[String]
  final private val characterTagList = new util.ArrayList[String]
  final private val E = new util.HashMap[String, util.List[String]]

}