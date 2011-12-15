
var a = Array(2,3,4)

println( a.fold(0)(_ + _) )

println( a.reduce(_ + _) )
