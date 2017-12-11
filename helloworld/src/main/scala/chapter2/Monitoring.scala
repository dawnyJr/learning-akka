import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}

object Monitoring extends App{
  val system = ActorSystem("Monitoring")
  val athena = system.actorOf(Props[Athena],"athena")
  val ares = system.actorOf(Props(classOf[Ares],athena),"ares")
  athena ! "akka"
  Thread.sleep(1000)
  system.terminate()
}

class Ares(athena: ActorRef) extends Actor{
  override def receive: Receive = {
    case Terminated(ref) =>
      println(s"Ares received terminated from $ref")
      context.stop(self)
  }

  override def preStart(): Unit = {
    super.preStart()
    context.watch(athena)
  }

  override def postStop(): Unit = {
    super.postStop()
    println("Ares postStop ...")
  }
}

class Athena extends Actor{
  override def receive: Receive = {
    case msg =>
      println(s"Athena received ${msg}")
      context.stop(self)
  }
}
