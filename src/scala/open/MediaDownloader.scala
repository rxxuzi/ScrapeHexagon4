package open

import global.GlobalProperties
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.io._
import java.net.{MalformedURLException, URL}
import java.util
import java.util.concurrent.atomic.AtomicInteger
import scala.annotation.unused
import scala.io.Source

/**
 * HTTP リクエストからhtmlをgetし、imgのソースURLから
 * 画像をゲットする
 */
object MediaDownloader {
  private val fileDir = "./output/scala/"
  private val ext = ".png"
  val count = new AtomicInteger

  //image downloader
  def downloadImage(fileStreamURL: String, saveAsFile: String): Unit = {
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

  // set fileFormat
  private def fileFormat(ex: String) = if (ex.endsWith("gif")) ".gif"
  else if (ex.endsWith("webm")) ".webm"
  else if (ex.endsWith("mp4")) ".mp4"
  else if (ex.endsWith("mov")) ".mov"
  else if (ex.endsWith("wmv")) ".wmv"
  else ext
}

final class MediaDownloader(srcURL: String) {


  def run(): Unit = {

    open.SRC.isRunning.set(MediaDownloader.count.get < open.SRC.MAX_IMG_CNT)
    try{
      val url = new URL(srcURL)

    if (SRC.isRunning.get) try {
      // HTTP URL Connection
      val html = Source.fromInputStream(url.openStream()).mkString
      val document = Jsoup.parse(html)

      //            System.out.println(document.title());
      if (GlobalProperties.TAG2JSON) tagList(document)
      //get #image img element
      val imageElement = document.getElementById("image")
      if (imageElement != null) {
      val imageUrl = imageElement.attr("src")
      //                System.out.println("Image URL: " + imageUrl);
      var fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
      // remove fileformat extension
      fileName = fileName.substring(0, fileName.lastIndexOf("."))
      val fileFormat = MediaDownloader.fileFormat(imageUrl)
      val filepath = MediaDownloader.fileDir + fileName + fileFormat
      // 画像をダウンロードして保存する
      MediaDownloader.downloadImage(imageUrl, filepath)
      MediaDownloader.count.set(MediaDownloader.count.get + 1)
    }
    else System.out.println("Image not found.")
    } catch {
      case e: IOException =>
      e.printStackTrace()
      case e: Exception =>

      //            System.out.println(e.getMessage());
      e.printStackTrace()
    }
    }catch {
      case e: MalformedURLException =>
        e.printStackTrace()
    } finally System.out.println(MediaDownloader.count.get + " : Download image : " + srcURL)
  }

  private val generalTagList = new util.ArrayList[String]
  private val characterTagList = new util.ArrayList[String]
  @unused
  private val sd = new util.LinkedList[String]

  private def tagList(doc: Document): Unit = {
    val generalElements = doc.getElementsByClass("tag-type-0")
    val characterElements = doc.getElementsByClass("tag-type-4")

    for (i <- 0 until generalElements.size) {
      val e = generalElements.get(i).getElementsByClass("search-tag")
      generalTagList.add(e.text)
    }
    for(i <- 0 until characterElements.size()){
      val e = characterElements.get(i).getElementsByClass("search-tag")
      characterTagList.add(e.text)
    }
  }
}