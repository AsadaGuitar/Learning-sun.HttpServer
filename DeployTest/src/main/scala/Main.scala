import com.example.app.api.APIServer

object Main extends App{

//  private val url = "jdbc:mysql://localhost:3306/money_keep"
//  private val username = "root"
//  private val password = "Asd18894"
//
//  val dao = DatabaseAccessory(url, username, password)
//
//  val sqlFormat = "INSERT INTO users(name, password) VALUES('%s', '%s');"
//  val sql = sqlFormat.format("name", "pass")
//  println(sql)
//
//  dao.close()

  for {
    s <- APIServer.startServer()
  } yield s

}
