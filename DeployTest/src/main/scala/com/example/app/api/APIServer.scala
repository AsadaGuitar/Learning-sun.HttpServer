package com.example.app.api

import com.example.app.api.home.{CostAPI, EntryAPI, MoneyAPI}
import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import java.time.format.DateTimeFormatter
import java.time.{ZoneOffset, ZonedDateTime}

object APIServer {

  sealed abstract class RequestMethod
  case object GET   extends RequestMethod
  case object POST  extends RequestMethod
  case object PUT   extends RequestMethod

  case class Request(header: java.util.Set[java.util.Map.Entry[String, java.util.List[String]]],
                     body  : Option[String])

  def startServer(): Either[IOException,Unit] ={

    val port = 8000

    val server = try {
      val httpServer = HttpServer.create(new InetSocketAddress(port), 0)
      Right(httpServer)
    } catch {case e: java.io.IOException => Left(e)}

    for {
      s <- server
    } yield {
      s.createContext("/", new APIHandler())
      s.start()
    }
  }

  final class APIHandler extends HttpHandler {

    private def homeHandler(path: Array[String], method: RequestMethod, request: Request)
    : Option[String] = path match {
      case x if x.isEmpty => None
      case x => x.head match {
        case "entry" => method match {
          case GET =>
            EntryAPI.get()
            None
          case POST =>
            EntryAPI.post(request)
            None
          case PUT => None
        }
        case "money" => method match {
          case GET => if (x.tail.isEmpty) {
            None
          }
          else {
            val child = x.tail.head
            if (child.matches("\\d{1,11}")) {
              MoneyAPI.get(child.toInt)
            }
            else {
              None
            }
          }
          case POST => MoneyAPI.post(request)
          case PUT =>
            MoneyAPI.put(request)
            None
        }
        case "costs" => method match {
          case GET => CostAPI.get(request)
          case POST => CostAPI.post(request)
          case PUT => None
        }
      }
    }

    override def handle(exchange: HttpExchange): Unit = {

      //通信情報
      val requestMethod = exchange.getRequestMethod match {
        case "GET"  => GET
        case "POST" => POST
        case "PUT"  => PUT
      }
      val requestURI    = exchange.getRequestURI
      val protocol      = exchange.getProtocol

      println(
        s"""
          |*************************************
          |URI      = $requestURI
          |METHOD   = $requestMethod
          |PROTOCOL = $protocol
          |""".stripMargin)

      //リクエストヘッダー
      val requestHeader = {
        val header = exchange.getRequestHeaders
        header.entrySet()
      }

      println("RequestHeader:")
      requestHeader.forEach(y => println(y))

      //リクエストボディ
      val requestBody: Option[String] ={
        val is = exchange.getRequestBody
        val byteData = is.readAllBytes()
        is.close()
        if(byteData.isEmpty) None
        else Some(new String(byteData))
      }

      println("\nRequestBody: ")
      requestBody.foreach(x => println(x))


      //パスを取得
      val url = requestURI.getPath
      val paths = url.split("/").tail
      println(s"url is $url\npaths is ${paths.mkString("Array(", ", ", ")")}")

      if (paths.isEmpty) {
        println("Could not path.")
        return
      }
      if (paths.head != "home") {
        println("path is illegal.")
        return
      }

      val msg: Option[String] =
        homeHandler(paths.tail, requestMethod, Request(requestHeader,requestBody))

      if (msg.isEmpty) {
        println("msg is nothing")
        return
      }

      //レスポンスヘッダを作成
      val resHeaders = exchange.getResponseHeaders
      resHeaders.set("Content-Type", "application/json")
      resHeaders.set("Last-Modified",
        ZonedDateTime
          .now(ZoneOffset.UTC)
          .format(DateTimeFormatter.RFC_1123_DATE_TIME))

      //サーバー内容を設定
      resHeaders.set("Server", "MyServer ("
        + System.getProperty("java.vm.name") + " "
        + System.getProperty("java.vm.vendor") + " "
        + System.getProperty("java.vm.version") + ")")

      //レスポンスボディ
      val resBody = msg.get

      // レスポンスヘッダを送信
      val statusCode = 200
      val contentLength = resBody.getBytes(StandardCharsets.UTF_8).length
      exchange.sendResponseHeaders(statusCode, contentLength)

      // レスポンスボディを送信
      val os = exchange.getResponseBody
      os.write(resBody.getBytes)
    }

  }
}


