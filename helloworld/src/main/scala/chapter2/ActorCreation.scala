package chapter2

import akka.actor.{Actor, ActorSystem, Props}
import chapter2.MusicController.{Play, Stop}
import chapter2.MusicPlayer.{StartMusic, StopMusic}




//Music Controller Messages
object MusicController{
  sealed trait ControllerMsg
  case object Play extends ControllerMsg
  case object Stop extends ControllerMsg
  def props = Props[MusicController]
}

//Music Controller
class MusicController extends Actor{
  override def receive = {
    case Play => println("Music started ...........")
    case Stop => println("Music stopped ...........")
  }

  override def preStart(): Unit = {
    super.preStart()
    println("MusicController Prestart() .........")
  }
}
//Music Player Messages
object MusicPlayer{
  sealed trait PlayMsg
  case object StartMusic extends PlayMsg
  case object StopMusic extends PlayMsg
}
//Music Player
class MusicPlayer extends Actor{
  override def receive = {
    case StartMusic =>
      val controller = context.actorOf(MusicController.props,"controller")
      controller ! Play
    case StopMusic =>
      println("I don't want to stop music")
    case _ => println("Unknown Message")
  }
}
object ActorCreation extends App{
  //Create the 'creation' Actor system
  val system = ActorSystem("creation")
  //Create the musicPlayer Actor
  val musicPlayer = system.actorOf(Props[MusicPlayer],"MusicPlayer")
  //Send StartMusic
  musicPlayer ! StartMusic
  //Send StopMusic
  musicPlayer ! StopMusic
  //terminate the actor system
  system.terminate()
}
