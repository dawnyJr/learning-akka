package chapter3.actorPath

import akka.actor.{Actor, ActorIdentity, ActorRef, Identify}

class Watcher extends Actor{
  var selection = context.actorSelection("/user/counter")
  selection ! Identify(None)
  override def receive = {
    case ActorIdentity(_,Some(ref)) => println(s"Actor Reference for counter is ${ref}")
    case ActorIdentity(_,None)      => println(s"Actor Selection for actor doesn't live :( ")
  }
}
