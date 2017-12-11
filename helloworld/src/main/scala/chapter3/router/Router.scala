package chapter3.router

import akka.actor.{Actor, ActorRef, Props}

class RouterPool extends Actor{

  var routees: List[ActorRef] = _

  override def preStart(): Unit = {
    super.preStart()
    routees = List.fill(5)(context.actorOf(Props[Worker]))
  }

  override def receive = {
    case msg =>
      println(s"I am A Router and I received a Message ...")
      routees(util.Random.nextInt(routees.size)) forward msg
  }
}

class RouterGroup(routees: List[String]) extends Actor{
  override def receive = {
    case msg =>
      println(s"I am A Router and I received a Message ...")
      context.actorSelection(routees(util.Random.nextInt(routees.size))) forward msg
  }
}
