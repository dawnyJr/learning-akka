package chapter4

import akka.actor.{ActorSystem, Props}
import chapter4.Counter.{Cmd, Decrement, Increment}

object Persistent extends App{

  val system = ActorSystem("persistent-actor")
  val counter = system.actorOf(Props[Counter])

  counter ! Cmd(Increment(3))
  counter ! Cmd(Increment(5))
  counter ! Cmd(Decrement(3))

  counter ! "print"

  Thread.sleep(1000)
  system.terminate()
}
