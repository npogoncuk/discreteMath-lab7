package com.company

fun main() {
    val graph = Graph.createGraph()
    println("The length of the shortest path from v1 to v5 using Dijkstra Algorithm: ${graph.dijkstraShortestPath("v1", "v5")}")
    val (mapWeight, mapPath) = graph.dijkstraShortestPath("v1")
    println("The length of the shortest path from v1 to other vertexes using Dijkstra Algorithm: $mapWeight")
    println("The way from v1 to other vertexes using Dijkstra Algorithm: $mapPath")
    println("The length of the shortest path from v1 to v5 using FloydWarshall Algorithm: ${ graph.getDistanceByFloydWarshall("v1", "v5") }")
    println("The length of the shortest path from vertex to all other vertexes: ${graph.floydWarshallAlgorithm().map { it.toList() }.toList() }")


}