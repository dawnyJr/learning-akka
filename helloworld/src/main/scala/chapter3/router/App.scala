package chapter3.router

import akka.actor.{ActorSystem, Props}
import chapter3.router.Worker.Work

object App extends App{

  val system = ActorSystem("Routing")
  val router = system.actorOf(Props[RouterPool])
  router ! Work()
  router ! Work()
  router ! Work()
  Thread.sleep(100)
  system.actorOf(Props[Worker],"w1")
  system.actorOf(Props[Worker],"w2")
  system.actorOf(Props[Worker],"w3")
  system.actorOf(Props[Worker],"w4")
  system.actorOf(Props[Worker],"w5")

  val workers = List(
    "/user/w1",
    "/user/w2",
    "/user/w3",
    "/user/w4",
    "/user/w5"
  )
  val routerGroup = system.actorOf(Props(classOf[RouterGroup],workers))
  routerGroup ! Work()
  routerGroup ! Work()
  Thread.sleep(100)
  system.terminate()
}
