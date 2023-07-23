import java.io.{FileOutputStream, InputStream, OutputStream}
import java.net.{HttpURLConnection, URL}

trait Online {
  val fileName: String
  def online: Boolean
  def httpRequest(url: String): String
  def download(url_Str: String): Unit = {
    val url = new URL(url_Str)
    val connection = url.openConnection.asInstanceOf[HttpURLConnection]
    connection.setRequestMethod("GET")
    connection.connect()
    val inputStream = connection.getInputStream
    val outputStream = new FileOutputStream(fileName)
    val buffer = new Array[Byte](4096)
    var bytesRead = 0
    while ( {
      bytesRead = inputStream.read(buffer); bytesRead != -1
    }) {
      outputStream.write(buffer, 0, bytesRead)
    }


    outputStream.close()
    inputStream.close()

    connection.disconnect()
  }
}
