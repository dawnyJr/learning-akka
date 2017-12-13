package chapter3.hotswapBehavior

import akka.actor.{Actor, ActorSystem, Props, Stash}
import chapter3.hotswapBehavior.UserStorage.DBOperation.Create
import chapter3.hotswapBehavior.UserStorage.{Connect, Disconnect, Operation}

object BecomeHotSwap extends App{
  val system = ActorSystem("hotswap")
  val userStorage = system.actorOf(Props[UserStorage],"userStorage")

  userStorage ! Operation(Create,Option(User("akka","akka@gmail.com")))
  userStorage ! Connect
  userStorage ! Disconnect
  Thread.sleep(100)
  system.terminate()

}

case class User(username: String, email: String)
object UserStorage{
  trait DBOperation
  object DBOperation{
    case object Create extends DBOperation
    case object Update extends DBOperation
    case object Read extends DBOperation
    case object Delete extends DBOperation
  }
  case object Connect
  case object Disconnect
  case class Operation(dBOperation: DBOperation, user: Option[User])
}
class UserStorage extends Actor with Stash{
  override def receive = disconnected
  def connected: Receive = {
    case Disconnect =>
      println(s"User storage disconnect from DB")
      context.unbecome()
    case Operation(op,user) =>
      println(s"User Storage receive ${op} to do in user: ${user}")

  }
  def disconnected: Receive = {
    case Connect =>
      println(s"User storage connected to DB")
      unstashAll()
      context.become(connected)
    case _ =>
      stash()
  }
}
