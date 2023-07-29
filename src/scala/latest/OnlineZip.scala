package latest

import crawler.OpenSRC
import data.{Archive, CheckImage}
import fast.Del
import global.GlobalProperties

import java.io.IOException
import java.util.{InputMismatchException, Scanner}

object OnlineZip {
  def main(args: Array[String]): Unit = {
    Del.allPicDelete()
    val sc = new Scanner(System.in)
    print("Enter the number of images you want to download: ")
    val properties = new GlobalProperties
    properties.makeDir()
    var x = -1
    try {
      x = sc.nextInt
      GlobalProperties.Compare(x)
    } catch {
      case e: InputMismatchException =>
        println("Wrong input")
        System.exit(1)
    }
    val opensrc = new OpenSRC
    println("Enter the Word")
    val word = sc.next
    val startTime = System.currentTimeMillis
    opensrc.setTag(word)
    opensrc.run()
    val endTime = System.currentTimeMillis
    println("Total execution time: " + (endTime - startTime))
    val ch = new CheckImage
    println("Download complete")
    val ar = new Archive
    try ar.createZip()
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }
  }
}