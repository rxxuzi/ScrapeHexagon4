package open

import java.util.Scanner
import fast.Tag
object ScalaMain {
  var IMG = 10

  def main(args: Array[String]): Unit = {
    println("Hello Scala ! version : " + scala.util.Properties.versionString)
    val sc = new Scanner(System.in)
    print("Enter the number of images you want to download: ")
    try IMG = sc.nextInt
    val sc2 = new Scanner(System.in)
    print("Enter the tag you want to download: ")
    val t = sc2.nextLine


    val openSRC = new SRC()
    val startTime = System.currentTimeMillis
    openSRC.setTag(Tag.translate(t))
    openSRC.run()
    val endTime = System.currentTimeMillis
    println("Total execution time: " + (endTime - startTime))
  }
}