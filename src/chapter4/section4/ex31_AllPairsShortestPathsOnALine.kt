package chapter4.section4

/**
 * 线图中任意顶点对之间的最短路径
 * 给定一幅加权线图（无向连通图，处理两个端点度数为1之外所有顶点的度数为2），
 * 给出一个算法在线性时间内对图进行预处理并在常数时间内返回任意两个顶点之间的最短路径。
 *
 * 解：以一个端点为起点，遍历加权线图，计算每个顶点到起点的距离
 * 顶点v到顶点w的距离为顶点w到起点的距离减去顶点v到起点的距离
 */
class LineGraph(val V: Int) {
    init {
        // 线图至少有两个端点
        require(V > 1)
    }

    // 两个点之间的权重默认为0，V个顶点有V-1条边
    private val edges = DoubleArray(V - 1)

    fun setWeight(startVertex: Int, weight: Double) {
        require(startVertex in 0 until V - 1)
        edges[startVertex] = weight
    }

    fun getWeight(startVertex: Int): Double {
        require(startVertex in 0 until V - 1)
        return edges[startVertex]
    }
}

class LineGraphSP(graph: LineGraph) {
    private val dist = DoubleArray(graph.V)

    init {
        dist[0] = 0.0
        for (v in 0 until graph.V - 1) {
            dist[v + 1] = dist[v] + graph.getWeight(v)
        }
    }

    fun dist(v: Int, w: Int): Double {
        return dist[w] - dist[v]
    }
}

fun main() {
    val graph = LineGraph(5)
    graph.setWeight(0, 1.0)
    graph.setWeight(1, 2.0)
    graph.setWeight(2, -1.0)
    graph.setWeight(3, 3.0)
    val sp = LineGraphSP(graph)
    for (v in 0 until graph.V) {
        for (w in v until graph.V) {
            println("v=$v w=$w weight=${sp.dist(v, w)}")
        }
    }
}