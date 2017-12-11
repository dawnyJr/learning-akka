import akka.actor.{Actor, ActorSystem, Props}


//define Actor messages
case class WhoToGreet(who: String)

//define Greeter Actor
class Greeter extends Actor {
  override def receive ={
    case WhoToGreet(who) => println(s"Hello $who")
  }

  override def preStart(): Unit = {
    super.preStart()
    println("call ----> preStart()")
  }

  override def postStop(): Unit = {
    super.postStop()
    println("call ----> preStop()")
  }
}

object HelloAkkaScala extends App{
  //Create the 'Hello akka' actor system
  val system = ActorSystem("hello-akka")
  //Create the 'greeter' actor
  val greeter = system.actorOf(Props[Greeter],"greeter")
  //Send WhoToGreet Message to actor
  greeter ! WhoToGreet("akka")

  //terminate the Actor system
  system.terminate()
}
