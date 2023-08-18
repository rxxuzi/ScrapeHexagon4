package _base18

import crawler.OpenHTML
import global.GlobalProperties

import java.io.File
import java.util.{Objects, Scanner}
import scala.Console.{GREEN, YELLOW}
import scala.collection.convert.ImplicitConversions._
import scala.collection.mutable
import scala.io.StdIn

object OpenMg {
  def main(args: Array[String]): Unit = {
    val scanner = new Scanner(System.in)
    println("Enter the tag: ")
    val tag = scanner.nextLine
    val v_url = "https://momon-ga.com/tag/" + tag + "/page/"

    print(YELLOW + "MAX PAGES")
    val maxPage = scanner.nextInt()
    for (i <- 1 to maxPage) {
      val url = v_url + i
      val openHTML = new OpenHTML
      val doc = openHTML.html(url)
      val posts = doc.getElementsByClass("post-list")
      val pages = posts.first.getElementsByTag("a")
      val d18s = mutable.ListBuffer.empty[OpenDL18]
      println(pages.size)


      pages.zipWithIndex.foreach { case (page, _) =>
        val title = page.getElementsByTag("span").text
        val dir = new File(GlobalProperties.BASE_DIR + title)

        if (dir.exists) println("Skip")
        else {
          val link = page.select("a").attr("abs:href")
          d18s += new OpenDL18(link, true)
        }
      }

      d18s.foreach(_.start())
      d18s.foreach(_.join())

    }
    println( GREEN + "Done")
    scanner.close()
    System.exit(0)
  }
}