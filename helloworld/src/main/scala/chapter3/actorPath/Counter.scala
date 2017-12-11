package chapter3.actorPath

import akka.actor.{Actor, ActorIdentity}

class Counter extends Actor{
  import Counter._

  var count = 0
  override def receive = {
    case Inc(n) => count += n
    case Dec(n) => count -= n
  }
}
object Counter{
  final case class Inc(num :Int)
  final case class Dec(num: Int)
}
