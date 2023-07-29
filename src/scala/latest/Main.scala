package latest

import data.Archive
import data.CheckImage
import fast.Del
import global.GlobalProperties
import global.Status
import java.io.IOException
import java.util.InputMismatchException
import java.util.Scanner

object Main {
  def main(args: Array[String]): Unit = {
    Del.allPicDelete()
    val sc = new Scanner(System.in)
    System.out.print("Enter the number of images you want to download: ")
    val properties = new GlobalProperties
    properties.makeDir()
    var x = -1
    try {
      x = sc.nextInt
      GlobalProperties.Compare(x)
    } catch {
      case e: InputMismatchException =>
        System.out.println("Wrong input")
        System.exit(1)
    }
    val opensrc = new OpenSRC
    System.out.println("Enter the Word")
    val word = sc.next
    val startTime = System.currentTimeMillis
    opensrc.setTag(word)
    opensrc.run()
    val endTime = System.currentTimeMillis
    System.out.println("Total execution time: " + (endTime - startTime))
    val ch = new CheckImage
    if (ch.dirLength == x) {
      System.out.println("Download complete")
      val ar = new Archive
      try ar.createZip()
      catch {
        case e: IOException =>
          throw new RuntimeException(e)
      }
    }
    else if (ch.dirLength < x) {
      System.out.println("Download Failed" + "\n" + "Status Code : 100")
      Status.setStatusCode(100)
    }
  }
}