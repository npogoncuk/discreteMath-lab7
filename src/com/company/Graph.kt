package com.company

import java.util.*
import kotlin.math.min

class Graph {
    private data class Vertex(val label: String): Comparable<Vertex> {
        var predecessor: Vertex? = null
        var weightToGet: Double = Double.POSITIVE_INFINITY
        var mark: Mark = Mark.TEMPORARY
        override fun compareTo(other: Vertex): Int {
            return weightToGet.compareTo(other.weightToGet)
        }
    }
    enum class Mark {
        CONSTANT,
        TEMPORARY
    }
    private data class EdgeEndWeight(val endVertex: Vertex, val weight: Double)

    private val vertices = mutableListOf<Vertex>()
    private val adjEdge: MutableMap<Vertex, MutableList<EdgeEndWeight>> = mutableMapOf()
    private var canBeUsedForDijkstraAlgorithm = true
    fun addVertex(label: String) {
        val vertex = Vertex(label)
        if (vertex !in vertices) vertices.add(vertex)
        adjEdge.putIfAbsent(vertex, mutableListOf())
    }
    fun addEdge(label1: String, label2: String, weight: Double) {
        if (weight < 0) canBeUsedForDijkstraAlgorithm = false
        val v1 = Vertex(label1)
        val v2 = Vertex(label2)
        adjEdge[v1]?.add(EdgeEndWeight(v2, weight)) ?: throw IllegalArgumentException("You didn't add vertex")
        adjEdge[v2]?.add(EdgeEndWeight(v1, weight)) ?: throw IllegalArgumentException("You didn't add vertex")
    }
    companion object {
        fun createGraph(): Graph {
            val graph = Graph()
            graph.addVertex("v1")
            graph.addVertex("v2")
            graph.addVertex("v3")
            graph.addVertex("v4")
            graph.addVertex("v5")
            graph.addVertex("v6")
            graph.addVertex("v7")
            graph.addVertex("v8")
            graph.addEdge("v1", "v3", 1.0)
            graph.addEdge("v1", "v4", 2.0)
            graph.addEdge("v4", "v5", 8.0)
            graph.addEdge("v4", "v6", 3.0)
            graph.addEdge("v5", "v6", 4.0)
            graph.addEdge("v1", "v7", 20.0)
            graph.addEdge("v1", "v8", 10.0)
            graph.addEdge("v1", "v2", 7.0)
            graph.addEdge("v8", "v7", 1.0)
            graph.addEdge("v8", "v2", 6.0)
            graph.addEdge("v2", "v7", 1.0)
            return graph
        }
    }
    fun dijkstraShortestPath(from: String, to: String): Double {
        if (!canBeUsedForDijkstraAlgorithm) throw IllegalArgumentException("This graph can't use Dijkstra's algorithm")
        if (Vertex(from) !in vertices || Vertex(to) !in vertices) throw IllegalArgumentException("Vertex doesn't exist")
        var current = getVertex(from)
        current.weightToGet = 0.0
        current.mark = Mark.CONSTANT
        while (current != getVertex(to)) {
            val adjVertices = vertices.filter { it.mark == Mark.TEMPORARY }
            for (vertex in adjVertices) {
                val newWeight = min(vertex.weightToGet, current.weightToGet + weightBetweenAdj(current, vertex))
                if (vertex.weightToGet != newWeight) vertex.predecessor = current
                vertex.weightToGet = newWeight
            }
            Collections.sort(adjVertices)
            val vertexMinWeight = adjVertices.first()
            vertexMinWeight.mark = Mark.CONSTANT
            current = vertexMinWeight
        }
        for (vertex in vertices) vertex.mark = Mark.TEMPORARY
        return current.weightToGet
    }
    fun dijkstraShortestPath(from: String): Pair<Map<String, Double>, Map<String, List<String>>> {
        fun getListOfPredecessor(v: Vertex): List<String> {
            fun getPredecessor(cur: Vertex): Vertex {
                return vertices[vertices.indexOf(cur)].predecessor!!
            }
            val list = mutableListOf<String>()
            var path: Vertex = v
            while (path != getVertex(from) ){
                list.add(path.label)
                path = getPredecessor(path)
            }
            list.add(path.label)
            return list.reversed()
        }
        val mapWeight = mutableMapOf<String, Double>()
        val mapPath = mutableMapOf<String, List<String>>()
        for (vertex in vertices) {
            mapWeight.putIfAbsent(vertex.label, dijkstraShortestPath(from, vertex.label))
            mapPath.putIfAbsent(vertex.label, getListOfPredecessor(vertex))
        }
        return Pair(mapWeight, mapPath)
    }
    private fun getVertex(label: String) = vertices[vertices.indexOf(Vertex(label))]
    private fun weightBetweenAdj(vertex1: Vertex, vertex2: Vertex): Double {
        if (vertex1 == vertex2) return 0.0
        val edges: MutableList<EdgeEndWeight> = adjEdge[vertex1]!!
        for (edge in edges) {
            if (edge.endVertex == vertex2) return edge.weight
        }
        return Double.POSITIVE_INFINITY
    }

    fun floydWarshallAlgorithm(): Array<Array<Double>> {
        val dist = Array(vertices.size) { Array(vertices.size) { 0.0 } }
        for (i in dist.indices) {
            for (j in dist[0].indices) {
                dist[i][j] = weightBetweenAdj(Vertex("v${i + 1}"), Vertex("v${j + 1}"))
            }
        }
        for (k in dist.indices) {
            for (i in dist.indices) {
                for (j in dist.indices) {
                    if (dist[i][j] > dist[i][k] + dist[k][j]) dist[i][j] = dist[i][k] + dist[k][j]
                }
            }
        }
        return dist
    }
    fun getDistanceByFloydWarshall(label1: String, label2: String): Double {
        val num1: Int = label1.filter { it.isDigit() }.toInt()
        val num2: Int = label2.filter { it.isDigit() }.toInt()
        return floydWarshallAlgorithm()[num1-1][num2-1]
    }
}
