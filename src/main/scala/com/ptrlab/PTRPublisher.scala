package com.ptrlab

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

case class Publish(message: String, topic: String) extends MySerialization

class PTRPublisher extends Actor with ActorLogging{

  val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    var message = AggregatorMessage(1 ,"pere")
    println("That 's remote:" + remoteActor)
    remoteActor ! message
    message = AggregatorMessage(2, "mere")
    remoteActor ! message
    message = AggregatorMessage(2, "pere")
    remoteActor ! message
    message = AggregatorMessage(1, "mere")
    remoteActor ! message
    message = AggregatorMessage(3, "mere")
    remoteActor ! message
    message = AggregatorMessage(4, "mere")
    remoteActor ! message
    message = AggregatorMessage(5, "mere")
    remoteActor ! message
    message = AggregatorMessage(1, "struguri")
    remoteActor ! message
  }
  override def receive: Receive = {

    case msg: String => {
      println("got message from broker: " + msg)
    }
  }
}
object PTRPublisher {

  def main(args: Array[String]) {

    val config = ConfigFactory.load.getConfig("RemotePublisher")
    val system = ActorSystem("ClientSystemP",config)
    val localActor = system.actorOf(Props[PTRPublisher], name="publisher")
  }


}