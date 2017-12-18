package chapter5

import akka.actor.Actor

class Worker extends Actor{
  import Worker._
  override def receive = {
    case msg: Work =>
      println(s"I received Work Message and my ActorRef: ${self}")
  }
}

object Worker{
  case class Work(message: String)
}