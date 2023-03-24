package com.ptrlab

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

case class Subscribe(topic: String) extends MySerialization
case class Unsubscribe(topic: String) extends MySerialization
case class StartLoop()

class PTRActor extends Actor with ActorLogging{


  val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")


  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    println("That 's remote:" + remoteActor)
    self ! StartLoop()
  }
  override def receive: Receive = {
    case msg: StartLoop => {
        println("1: Subscribe to a given topic")
        println("2: Unsubscribe from given topic")
        println("3: Wait for message")
        println("Your order, sir?")
        val x : Int = StdIn.readInt()
        x match {
          case 1 =>
            println("Desired topic?: ")
            val topicName = StdIn.readLine()
            remoteActor ! Subscribe(topicName)
          case 2 =>
            println("Desired topic?: ")
            val topicName = StdIn.readLine()
            remoteActor ! Unsubscribe(topicName)
          case 3 =>
            println("Got it")
        }
    }
    case msg: String => {
      println("got message from broker: " + msg)
      self ! StartLoop()
    }
  }
}
object PTRActor {

  def main(args: Array[String]) {

    val config = ConfigFactory.load.getConfig("LocalActor")
    val system = ActorSystem("ClientSystem",config)
    val localActor = system.actorOf(Props[PTRActor], name="local")
  }

}