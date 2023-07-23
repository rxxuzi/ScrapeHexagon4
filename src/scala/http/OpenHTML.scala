package http

import java.io._
import java.net.{HttpURLConnection, URL}


object OpenHTML {
  def main(args: Array[String]): Unit = {
    try {
      val url = new URL("https://www.example.com")
      val openConnection = url.openConnection.asInstanceOf[HttpURLConnection]
      openConnection.setAllowUserInteraction(false)
      openConnection.setInstanceFollowRedirects(true)
      openConnection.setRequestMethod("GET")
      openConnection.connect()
      val httpStatusCode = openConnection.getResponseCode
      if (httpStatusCode != HttpURLConnection.HTTP_OK) throw new Exception("HTTP Status " + httpStatusCode)
      val contentType = openConnection.getContentType
      println("Content-Type: " + contentType)
      // Input Stream
      val dataInStream = new DataInputStream(openConnection.getInputStream)
      // Output Stream
      val dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("./resources/sample.html")))
      // Read Data
      val b = new Array[Byte](4096)
      var readByte = 0
      while (-1 != {
        readByte = dataInStream.read(b); readByte
      }) {
        dataOutStream.write(b, 0, readByte)
      }
      // Close Stream
      dataInStream.close()
      dataOutStream.close()
    } catch {
      case e: IOException =>
        e.printStackTrace()
      case e: Exception =>
        println(e.getMessage)
        e.printStackTrace()
    }
  }
}