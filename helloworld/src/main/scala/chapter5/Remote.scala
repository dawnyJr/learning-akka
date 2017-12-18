package chapter5

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object MembersService extends App{

  val config = ConfigFactory.load.getConfig("MembersService")
  val system = ActorSystem("MembersService",config)
  val worker = system.actorOf(Props[Worker],"remote-worker")
  println(s"Worker actor Path: ${worker.path}")
}

object MemberServiceLookup extends App{
  val config = ConfigFactory.load.getConfig("MemberServiceLookup")
  val system = ActorSystem("MemberService",config)
  val worker = system.actorSelection("akka.tcp://MembersService@127.0.0.1:2552/user/remote-worker")
  worker ! Worker.Work("Hi Remote Actor")
}

object MemberServiceRemoteCreation extends App{
  val config = ConfigFactory.load.getConfig("MembersServiceRemoteCreation")
  val system = ActorSystem("MemberServiceRemoteCreation",config)
  val worker = system.actorOf(Props[Worker],"workerActorRemote")
  println(s"The remote path of worker Actor: ${worker.path}")
  worker ! Worker.Work("Hi remote Actor")
}
