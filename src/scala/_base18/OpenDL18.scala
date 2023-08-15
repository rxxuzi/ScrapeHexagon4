package _base18

import crawler.OpenHTML
import global.GlobalProperties
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.io._
import java.net.{MalformedURLException, URL}
import java.util
import java.util.Objects
import scala.Console._
import scala.util.control.Breaks.break

object OpenDL18 {
  private val baseDir = GlobalProperties.BASE_DIR
  private val ext = GlobalProperties.FILE_FORMAT
  val skips: Array[String] = Array[String]("skip", "gay", "scat")
  val and : Array[String] = Array[String]()

  //image downloader
  @throws[IOException]
  private def downloadImage(imageUrl: String, fileName: String):Boolean = {
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
    return true
  }

  // set fileFormat
  private def fileFormat(ex: String) = if (ex.endsWith("gif")) ".gif"
  else if (ex.endsWith("webm")) ".webm"
  else if (ex.endsWith("mp4")) ".mp4"
  else if (ex.endsWith("mov")) ".mov"
  else if (ex.endsWith("wmv")) ".wmv"
  else ext
}

class OpenDL18 extends Thread {
  private var fileDir = GlobalProperties.PIC_DIR
  private var url: URL = null
  private var fileName: String = null
  private var imageUrl: String = null
  private[_base18] var document: Document = null
  private[_base18] var makeddir = false
  private[_base18] var saved = false
  private var saveImage = true
  private[_base18] val tags = new util.LinkedList[String]
  
  def this(s: String, saveImage: Boolean) {
    this()
    this.saveImage = saveImage
    try {
      this.url = new URL(s)
      document = OpenHTML.html(url)
    } catch {
      case e: MalformedURLException =>
        throw new RuntimeException(e)
    }
  }

  override def run(): Unit = {
    try {
      val pageTitle = document.getElementsByTag("h1")
      if (!skip) if (andSearch) {
        mkdir(pageTitle.get(0).text)
        val main = document.getElementById("post-hentai")
        if (main != null) {
          val pics = main.getElementsByTag("img")
          for(i <- 0 until pics.size()){
            imageUrl = pics.get(i).attr("src")
            fileName = pics.get(i).attr("alt")
            if (saveImage) saved = OpenDL18.downloadImage(imageUrl, getFilePath)
          }
        }
      }
      if (saved) println(GREEN + " SAVE " + RESET + pageTitle.get(0).text)
      else println( YELLOW + " SKIP " + RESET + pageTitle.get(0).text)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }

  private def andSearch = {
    var b = false
    if (OpenDL18.and.length >= 1) for (a <- OpenDL18.and) {
      if (tags.contains(a)) b = true
      else {
        b = false
        break //todo: break is not supported
      }
    }
    else b = true
    saved = b
    b
  }

  def skip: Boolean = {
    val tagTable = document.getElementsByClass("post-tag-table")
    for (i <- 0 until tagTable.size()) {
      val es: Elements = tagTable.get(i).getElementsByTag("a")
      for(j <- 0 until es.size()){
        val np = es.get(j).text
        for (word <- OpenDL18.skips) {
          if (Objects.equals(word, np)) {
            saved = false
            return true
          }
        }
        tags.add(np)
      }
    }
    false
  }

  private def mkdir(altName: String): Unit = {
    val dir = new File(OpenDL18.baseDir + altName)
    if (!dir.exists) if (dir.mkdir) {
      makeddir = true
    }
    fileDir = OpenDL18.baseDir + altName + "/"
  }

  private def getFilePath = {
    fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1)
    fileName = fileName.substring(0, fileName.lastIndexOf("."))
    val fileFormat = OpenDL18.fileFormat(imageUrl)
    fileDir + fileName + fileFormat
  }
}