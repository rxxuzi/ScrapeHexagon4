package zip

import data.Archive
import global.GlobalProperties
import java.io.IOException

object MkZip {
  def main(args: Array[String]): Unit = {
    val path = GlobalProperties.PIC_DIR
    val archive = new Archive(path)
    try archive.createZip()
    catch {
      case e: IOException =>
        throw new RuntimeException(e)
    }finally {
      println("Done!")
    }
  }
}