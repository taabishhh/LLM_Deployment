
@main
def main(): Unit = {
  val mp = Map("x"->1)
  mp.toMap.foreach(println)
  if mp.contains("y") then println(mp("y")) else println("no key y")
  val lst = List("hello, world", "xyz,,kk")
  val csv = lst.map {
    str=>str.replace(',', '-')
  }.mkString(",")
  println(csv)
}

