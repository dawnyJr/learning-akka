package chapter3.router

import akka.actor.Actor
import chapter3.router.Worker.Work

class Worker extends Actor{
  override def receive = {
    case msg: Work =>
      println(s"I received Work Message and My ActorRef: ${self}")
  }
}
object Worker{
  case class Work()
}
