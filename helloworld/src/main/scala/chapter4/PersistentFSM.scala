package chapter4

import akka.actor.{ActorSystem, Props}
import chapter4.Account.{CR, DR, Operation}

object PersistentFSM extends App{

  val system = ActorSystem("persistent-fsm-actor")
  val account = system.actorOf(Props[Account])
  account ! Operation(1000, CR)
  account ! Operation(10, DR)
  Thread.sleep(1000)
  system.terminate()

}
