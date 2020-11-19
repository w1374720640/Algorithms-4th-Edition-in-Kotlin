package chapter4.section1

/**
 * 使用dfs(0)处理由Graph的构造函数从tinyGex2.txt（请见练习4.1.2）得到的图
 * 并按4.1.3.5节的图4.1.14的样式给出详细的轨迹图。
 * 同时，画出edgeTo[]所表示的树
 *
 * 解：仅画出图，轨迹图省略
 */
fun main() {
    val graph = Graph(12)
    graph.addEdge(8, 4)
    graph.addEdge(2, 3)
    graph.addEdge(1, 11)
    graph.addEdge(0, 6)
    graph.addEdge(3, 6)
    graph.addEdge(10, 3)
    graph.addEdge(7, 11)
    graph.addEdge(7, 8)
    graph.addEdge(11, 8)
    graph.addEdge(2, 0)
    graph.addEdge(6, 2)
    graph.addEdge(5, 2)
    graph.addEdge(5, 10)
    graph.addEdge(5, 0)
    graph.addEdge(8, 1)
    graph.addEdge(4, 1)
    drawGraph(graph, showIndex = true)
}