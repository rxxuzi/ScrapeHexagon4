package latest

import fast.Tag
import global.GlobalProperties
import global.Status
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import scala.io.Source

/**
 * This class is a crawler of the OpenSRC website.
 * It can be used to download images from the website.
 *
 * <p>The following example illustrates how to use this class:
 *
 * <pre>
 * {@code
 * OpenSRC osrc = new OpenSRC(); //create a new crawler
 * osrc.setTag(exampleSearchedTag); //set tag
 * osrc.run();//run the crawler
 * }
 * </pre>
 * @author rxxuzi
 * @version 1.5
 */
object OpenSRC {
  val http: String = GlobalProperties.DOMAIN
  val BASIC_URL: String = GlobalProperties.DOMAIN + "posts?page="
  val MAX_IMG_CNT: Int = GlobalProperties.MAX_IMG_CNT
  var PAGE_COUNT = 1
  private val BASIC_TAG = "&tags="
  val isRunning = new AtomicBoolean(true)
  var imgCnt = 0
}

final class OpenSRC {
  private var TAG = "exusiai_%28arknights%29"
  private val px = 0

  def getTag: String = TAG

  def setTag(TAGNAME: String): Unit = {
    this.TAG = Tag.translate(TAGNAME)
  }

  def run(): Unit = {
    while (OpenSRC.isRunning.get) {
      System.out.println("run : " + OpenSRC.isRunning.get + ", count : " + OpenSRC.imgCnt + ", px : " + px)
      sendPage(OpenSRC.BASIC_URL + OpenSRC.PAGE_COUNT + OpenSRC.BASIC_TAG + TAG)
      OpenSRC.PAGE_COUNT += 1
    }
    System.out.println("Last finish")
  }

  def sendPage(page: String): Unit = {
    if (OpenSRC.isRunning.get) try {
      val url = new URL(page)
      System.out.println("images from : " + url)
      val html = Source.fromInputStream(url.openStream()).mkString
      val document = Jsoup.parse(html)
      System.out.println(document.title)
      //get #posts img element
      val posts = document.getElementsByClass("post-preview-link")
      var d: Array[Downloader] = null
      val fullLoop = OpenSRC.imgCnt + posts.size < OpenSRC.MAX_IMG_CNT
      if (posts.size == 0) {
        OpenSRC.isRunning.set(false)
        return
      }
      if (fullLoop) {
        d = new Array[Downloader](posts.size)
        System.out.println("fullLoop : " + fullLoop + ", count : " + OpenSRC.imgCnt + ", posts size : " + posts.size + " MAX_IMG_CNT : " + OpenSRC.MAX_IMG_CNT)
        // 並列処理for文
        for (i <- 0 until d.length) {
          val p = posts.get(i).attr("href")
          val link = OpenSRC.http + p
          if (OpenSRC.imgCnt < OpenSRC.MAX_IMG_CNT) {
            d(i) = new Downloader(link)
            d(i).start()
          }
        }
        for (dr <- d) {
          try dr.join()
          catch {
            case e: InterruptedException =>
              e.printStackTrace()
          }
        }
        OpenSRC.imgCnt += posts.size
      }
      else {
        val n = OpenSRC.MAX_IMG_CNT - OpenSRC.imgCnt
        System.out.println("fullLoop : " + fullLoop + ", count : " + OpenSRC.imgCnt + ", posts size :  " + n + " MAX_IMG_CNT : " + OpenSRC.MAX_IMG_CNT)
        d = new Array[Downloader](n)
        System.out.println("Not max saves!")
        // 並列処理for文
        var i = 0
        while (i < d.length && i < posts.size) {
          val p = posts.get(i).attr("href")
          val link = OpenSRC.http + p
          if (OpenSRC.imgCnt < OpenSRC.MAX_IMG_CNT) {
            d(i) = new Downloader(link)
            d(i).start()
          }
          i += 1
        }
        for (dr <- d) {
          try dr.join()
          catch {
            case e: InterruptedException =>
              e.printStackTrace()
          }
        }
        OpenSRC.imgCnt += n
      }
      if (OpenSRC.imgCnt >= OpenSRC.MAX_IMG_CNT) {
        OpenSRC.isRunning.set(false)
        System.out.println("img> max -> finish")
        Status.setStatusCode(0)
        Thread.sleep(1000)
      }
      System.out.println(OpenSRC.imgCnt + " images downloaded")
    } catch {
      case e: MalformedURLException =>
        System.out.println("MalformedURLException")
        e.printStackTrace()
      case e: InterruptedException =>
        System.out.println("InterruptedException")
        e.printStackTrace()
      case e: Exception =>
        e.printStackTrace()
    }
    else {
      System.out.println("isRunning = " + OpenSRC.isRunning)
      System.out.println("This is End")
    }
  }
}