package chapter4.section4

import chapter3.section5.LinearProbingHashSET
import extensions.formatStringLength

/**
 * 敏感度
 * 给定一幅加权有向图和一对顶点s和t，编写一个SP的用例对该图中的所有边进行敏感度分析：
 * 计算一个V*V的布尔矩阵，对于任意的v和w，当v->w为加权有向图中的一条边，
 * 且增加v->w的权重不会增加从s到t的最短路径的权重时，v行w列的值为true，否则为false。
 *
 * 解：根据SP对象求出从s到t的最短路径，只有当v->w在图中，但不在最短路径上时，v行w列的值才为true
 */
fun ex38_Sensitivity(digraph: EdgeWeightedDigraph, sp: SP, t: Int): Array<BooleanArray> {
    val array = Array(digraph.V) { BooleanArray(digraph.V) }
    val path = sp.pathTo(t) ?: return array
    val set = LinearProbingHashSET<DirectedEdge>()
    path.forEach {
        set.add(it)
    }
    digraph.edges().forEach { edge ->
        if (!set.contains(edge)) {
            array[edge.from()][edge.to()] = true
        }
    }
    return array
}

fun main() {
    val s = 0
    val t = 3
    println("s=$s t=$t")
    val digraph = getTinyEWD()
    val sp = DijkstraSP(digraph, s)
    println(sp.pathTo(t)?.joinToString())
    val array = ex38_Sensitivity(digraph, sp, t)
    // 格式化打印数组
    println(array.joinToString(separator = "\n") { booleanArray ->
        booleanArray.joinToString(separator = " ") { value ->
            formatStringLength(value.toString(), 5, alignLeft = true)
        }
    })
}