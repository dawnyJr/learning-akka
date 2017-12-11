package chapter3.actorPath

import akka.actor.{ActorSystem, PoisonPill, Props}

object App extends App{
  val system = ActorSystem("Actor-Paths")
  val counter1 = system.actorOf(Props[Counter],"counter")
  println(s"Actor Reference for counter1: ${counter1}")
  val counterSelection1 = system.actorSelection("counter")
  println(s"Actor Selection for conter1: ${counterSelection1}")
  counter1 ! PoisonPill
  Thread.sleep(2000)
  val counter2 = system.actorOf(Props[Counter],"counter")
  println(s"Actor Reference for counter2: ${counter2}")
  val counterSelection2 = system.actorSelection("counter")
  println(s"Actor Selection for counter2: ${counterSelection2}")
  println("**** Creating Actor Watcher ****")
  Thread.sleep(2000)
  val watcher = system.actorOf(Props[Watcher],"watcher")

  system.terminate()

}
