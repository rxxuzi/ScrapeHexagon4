package open

import fast.Tag
import global.GlobalProperties
import org.jsoup.Jsoup

import java.net.URL
import java.util.concurrent.atomic.{AtomicBoolean, AtomicReference}
import java.util.stream.IntStream
import scala.io.Source

object SRC {
  val http: String = "https://danbooru.donmai.us/"
  val BASIC_URL: String = "https://danbooru.donmai.us/" + "posts?page="
  val MAX_IMG_CNT: Int = Math.min(ScalaMain.IMG, GlobalProperties.MAX_IMG_CNT)
  var PAGE_COUNT = 1
  private val BASIC_TAG = "&tags="
  val isRunning = new AtomicBoolean(true)
}

final class SRC {
  private var TAG = "exusiai_%28arknights%29+pantyhose"

  def getTag: String = TAG

  def setTag(TAGNAME: String): Unit = {
    this.TAG = Tag.translate(TAGNAME)
  }

  def run(): Unit = {
    do {
      println(SRC.BASIC_URL + SRC.PAGE_COUNT + SRC.BASIC_TAG + TAG)
      sendPage(SRC.BASIC_URL + SRC.PAGE_COUNT + SRC.BASIC_TAG + TAG)
      SRC.PAGE_COUNT += 1
    } while (SRC.isRunning.get)
    System.out.println("Last finish")
  }

  def sendPage(page: String): Unit = {
    try {
      val url = new URL(page)

      val html = Source.fromInputStream(url.openStream()).mkString
      val document = Jsoup.parse(html)
      System.out.println(document.title)
      //get #posts img element
      val posts = document.getElementsByClass("post-preview-link")
      val downloader = new AtomicReference[MediaDownloader]
      val fullLoop = MediaDownloader.count.get + posts.size < SRC.MAX_IMG_CNT
      System.out.println("fullLoop : " + fullLoop + " count : " + MediaDownloader.count.get + " posts.size() : " + posts.size + " MAX_IMG_CNT : " + SRC.MAX_IMG_CNT)
      if (posts.size == 0) {
        SRC.isRunning.set(false)
        return
      }
      if (fullLoop) {
        // 並列処理for文
        IntStream.range(0, posts.size).forEach((i: Int) => {
          val p = posts.get(i).attr("href")
          val link = SRC.http + p
          if (MediaDownloader.count.get < SRC.MAX_IMG_CNT) {
            downloader.set(new MediaDownloader(link))
            downloader.get.run()
          }
        })
      }
      else {
        val n = SRC.MAX_IMG_CNT - MediaDownloader.count.get
        // 並列処理for文
        IntStream.range(0, n).forEach((i: Int) => {
          val p = posts.get(i).attr("href")
          val link = SRC.http + p
          if (MediaDownloader.count.get < SRC.MAX_IMG_CNT) {
            downloader.set(new MediaDownloader(link))
            downloader.get.run()
          }
        })
      }
      if (MediaDownloader.count.get >= SRC.MAX_IMG_CNT) {
        SRC.isRunning.set(false)
        Thread.sleep(1000)
      }
      System.out.println(MediaDownloader.count.get + " images downloaded")
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }
  }
}