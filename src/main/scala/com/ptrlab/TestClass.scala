package com.ptrlab

object TestClass extends App{
    println("Hello PTR")

    val ptrList = List("Hello", "PTR")
   for(element<-ptrList){
       println(element)
   }

}
