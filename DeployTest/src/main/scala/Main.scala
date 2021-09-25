import com.example.app.api.APIServer

object Main extends App{

  for {
    s <- APIServer.startServer()
  } yield s
}
