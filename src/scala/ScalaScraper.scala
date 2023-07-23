import java.io.PrintWriter
import java.net.URL
import scala.io.Source

object ScalaScraper {
  def main(args: Array[String]): Unit = {
    val url = new URL("https://scala-lang.org/")
    val html = Source.fromInputStream(url.openStream()).mkString
    // save as ./resources/math.html
    val file = new java.io.File("./resources/scala.html")
    Some(new PrintWriter(file)).foreach{p => p.write(html); p.close()}
  }
}

