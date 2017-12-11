import Aphrodite.{RestartException, ResumeException, StopException}
import akka.actor.SupervisorStrategy._
import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props}

import scala.concurrent.duration.DurationInt

object Supervision extends App{
  val system = ActorSystem("supervision")
  val hera = system.actorOf(Props[Hera],"Hera")

  //hera ! "Resume"
  //hera ! "Restart"
  hera ! "Stop"
  system.terminate()
}
class Aphrodite extends Actor{

  override def preStart(): Unit = {
    super.preStart()
    println("Aphrodite preStart hook ....")
  }
  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    super.preRestart(reason, message)
    println("Aphrodite preRestart hook ....")
  }
  override def postRestart(reason: Throwable): Unit = {
    super.postRestart(reason)
    println("Aphrodite postRestart hook ....")
  }
  override def postStop(): Unit = {
    super.postStop()
    println("Aphrodite postStop hook ....")
  }

  override def receive: Receive = {
    case "Resume" =>
      println("Aphrodite throw ResumeException")
      throw ResumeException
    case "Stop" =>
      println("Aphrodite throw StopException")
      throw StopException
    case "Restart" =>
      println("Aphrodite throw RestartException")
      throw RestartException
    case _ => throw new Exception
  }
}
object Aphrodite{
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}
class Hera extends Actor{

  var childRef: ActorRef = _
  override def supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 second){
    case ResumeException  => Resume
    case RestartException => Restart
    case StopException   => Stop
    case _: Exception     => Escalate
  }

  override def preStart(): Unit = {
    super.preStart()
    childRef = context.actorOf(Props[Aphrodite],"Aphrodite")
  }

  override def receive: Receive = {
    case msg =>
      println(s"Hera received ${msg}")
      childRef ! msg
      Thread.sleep(100)
  }
}
